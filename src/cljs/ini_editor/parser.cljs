(ns ini-editor.parser
  "Parse to and from ini files."
  (:require
   [machine-conf.utils :as utils]
   [cljs.reader :as reader]
   [clojure.string :as string]))


(defn- parse-ini-line
  "Helper function that parses a single line. It returns its type, as well as
  the result in a vector tuple. Here are the possibilities:
  [:blank nil]
  [:metadata hashmap]
  [:comment string]
  [:section section-string]
  [:key-value [key-string value-string]]
  [:parse-error nil], and an exception is thrown in this case."
  [l]
  (let [l (string/trim l)]
    (cond
      (string/blank? l)
      [:blank nil]

      (string/starts-with? l "#!")
      [:metadata (-> l (subs 2) reader/read-string)]

      (string/starts-with? l \#)
      [:comment (-> l (subs 1) string/trim)]

      (and (string/starts-with? l \[) (string/ends-with? l \]))
      [:section (subs l 1 (-> l count dec))]

      (string/includes? l \=)
      [:key-value (mapv string/trim (string/split l #"=" 2))]

      :else
      (do (throw (str "ini parse error: " l))
          [:parse-error nil]))))

(defn- helper-update-value
  "Updates the ini value based on its metadata. For most cases, this means
  assigning the section, key to the new value, but for multiline, it means
  adding the new value to the vector of old values.
  values - hashmap: section-string -> key-string -> value
  sec - section-string
  k - key-string
  v - value-string
  metadata - hashmap"
  [values sec k v metadata]
  (let [old-val (get-in values [sec k])]
    (cond
      (= (:type metadata) "multiline")
      (assoc-in values [sec k] (conj (or old-val []) v))

      :else
      (assoc-in values [sec k]
                (if (some? old-val) (conj old-val v) v)))))

(defn parse-ini
  "Parses the ini into an internal representation. Here are the fields of the
  returned hashmap:
  :key-metadata - hashmap: section-string -> key-string -> hashmap
  :key-order - hashmap: section-string -> [key-string]
  :section-metadata - hashmap: section-string -> hashmap
  :section-order - [section-string]
  :values - hashmap: section-string -> key-string -> value-string or [value-string]"
  [s]
  (loop [[l & r] (string/split-lines s)
         section "" ; string
         meta-acc {} ;; string -> any
         key-meta {} ;; string -> string -> hashmap
         key-order {} ;; string -> [string]
         section-meta {} ;; string -> hashmap
         section-order [] ;; [string]
         values {}] ;; string -> string -> string | [string]
    (if (nil? l) {:key-metadata key-meta
                  :key-order (reduce merge (map (fn [[k v]] {k (distinct v)}) key-order))
                  :section-metadata section-meta
                  :section-order (distinct section-order)
                  :values values}
        (let [[type result] (parse-ini-line l)]
          (cond
            (= type :metadata)
            (recur r ; lines remaining
                   section ; current section
                   (merge meta-acc result) ; accumulated metadata
                   key-meta ; key metadata
                   key-order ; key order
                   section-meta ; section metadata
                   section-order ; section order
                   values) ; values

            (= type :comment)
            (recur r ; lines remaining
                   section ; current section
                   (update meta-acc :comments utils/append-line result) ; accumulated metadata
                   key-meta ; key metadata
                   key-order ; key order
                   section-meta ; section metadata
                   section-order ; section order
                   values) ; values

            (= type :section)
            (recur r ; lines remaining
                   result ; current section
                   {} ; accumulated metadata
                   key-meta ; key metadata
                   (assoc key-order result []) ; key order
                   (assoc section-meta result meta-acc) ; section metadata
                   (conj section-order result) ; section order
                   (assoc values result {})) ; values

            (= type :key-value)
            (let [[k v] result]
              (recur r ; lines remaining
                     section ; current section
                     {} ; accumulated metadata
                     (update-in key-meta [section k] merge meta-acc) ; key metadata
                     (update key-order section conj k) ; key order
                     section-meta ; section metadata
                     section-order ; section order
                     (helper-update-value values section k v meta-acc))) ; values

            :else
            (recur r ; lines remaining
                   section ; current section
                   meta-acc ; accumulated metadata
                   key-meta ; key metadata
                   key-order ; key order
                   section-meta ; section metadata
                   section-order ; section order
                   values)))))) ; values

(defn- comments-to-str
  "Formats the given comment for writing to file. This involves putting \"# \"
  at the start of each line. This function is used by `metadata-to-str`"
  [s]
  (let [lines (string/split-lines s)
        formatted (map #(str "# " %1) lines)
        s (string/join \newline formatted)]
    (if (string/blank? s) "" (str s \newline))))

(defn- metadata-to-str
  "Converts metadata into a formatted string. This involves converting the
  hashmap into a string and appending the string \"#! \" to each line as
  well. For formatting purposes, each :key gets its own line. The comments are
  handled by the `comments-to-str` function"
  [m]
  (let [comments (:comments m)
        ks (filter #(not= %1 :comments) (keys m))]
    (str (comments-to-str comments)
     (string/join \newline
                  (map #(str "#! " {%1 (get m %1)}) ks)))))

(defn- ini-key-to-str
  "Given a section, key, and the data provided by the function `parse-ini-line`,
  the key-value pair  is returned in string representation.
  ini - hashmap with same fields as the return value of `parse-ini-line`.
  section - section-string
  k - key-string"
  [ini sec k]
  (let [meta (get-in ini [:key-metadata sec k])
        type (or (:type meta) "text")
        v (get-in ini [:values sec k])]
    (str (metadata-to-str meta)
         \newline
         (cond
           (= type "text") (str k \= v)
           (= type "options") (str k \= v)
           (= type "multiline") (string/join \newline (map #(str k \= %1) v))
           :else (throw (str "Unknown type"))))))

(defn- ini-sec-to-str
  "Given a section and the data provided by the function `parse-ini-line`,
  the section  is returned in string representation.
  ini - hashmap with same fields as the return value of `parse-ini-line`.
  section - section-string"
  [ini sec]
  (str (metadata-to-str (get-in ini [:section-metadata sec]))
       \[ sec \]
       \newline
       (string/join \newline
                    (map #(ini-key-to-str ini sec %1)
                         (get-in ini [:key-order sec])))
       \newline))

(defn ini-to-str
  "Given the same structured hashmap as returned by `parse-ini-line`."
  [ini]
  (string/join \newline
               (map #(ini-sec-to-str ini %1) (:section-order ini))))
