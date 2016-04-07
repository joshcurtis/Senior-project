(ns utils.core
  "Generic utility functions."
  (:require
   [clojure.set]
   [clojure.string :as string]))

(defonce mt (.-protobuf js/machinetalk))
(defonce container (.-Container (.-message mt)))
(defonce container-types (.-ContainerType (.-message mt)))

(defn alert
  "Shows an alert box with the provided arguments displayed as strings."
  ([arg] (js/alert (str arg)))
  ([& args] (js/alert (str args))))

(defn time-seconds
  "Returns a timestamp, in seconds."
  []
  (-> js/Date .now (/ 1000.0)))

(defn append-line
  "Appends new-line to old-lines. If old-lines is blank or nil, then new-line is
  returned"
  [old-lines new-line]
  (if (or (nil? old-lines) (string/blank? old-lines))
    new-line
    (str old-lines \newline new-line)))

(defn click-element
  "The DOM element with the id `id` is clicked."
  [id]
  (.click (.getElementById js/document id)))

(defn element-value
  "The value of the DOM element with the `id`."
  [id]
  (.-value (.getElementById js/document id)))

(defn read-file
  "`js-file-obj` must be a JavaScript `File` object and callback must be a
  function that takes a `string`. The contents of the `js-file-obj` as a
  `string` are passed to the provided `callback`. If it fails, then nothing
  happens."
  [js-file-obj callback]
  (let [reader (js/FileReader.)
        cback #(callback (-> %1 .-target .-result))]
    (aset reader "onload" cback)
    (.readAsText reader js-file-obj)))

(defn remove-idx
  "Given a `vector v`, `v` is returned with item at index `idx` removed."
  [v idx]
  (assert (vector? v) "v is not a vector")
  (vec (concat (subvec v 0 idx) (subvec v (inc idx)))))

(defn save-file
  "Saves the contents of `text` with a target filename of `filename`."
  [text filename]
  (assert (string? text))
  (assert (string? filename))
  (let [blob (js/Blob. #js [text] #js {"type" "text/plain;charset=utf-8"})]
    (js/saveAs blob filename)))

(defn toggle-membership
  "Given a `set s`, element `v` is removed if it is in the set, or added if it
  is not."
  [s v]
  (assert (set? s))
  (if (contains? s v)
    (clojure.set/difference s #{v})
    (clojure.set/union s #{v})))

;; file string helpers

(defn dir?
  "Returns true if the string represents a directory. This is checked by looking
  for the / character at the end."
  [s]
  (assert (string? s))
  (string/ends-with? s \/))

(defn fname-from-path
  "Returns the filename from the path, this is the element after the last /."
  [path]
  (last (string/split path \/)))

(defn file-ext
  "Returns the extension of the file. If there is no extension, then the blank
  string is returned. The extension of a file is whatever substring comes after
  the .(dot)."
  [s]
  (assert (string? s))
  (if (and (string/includes? s \.) (not (dir? s)))
    (-> s (string/split \.) last string/lower-case)))


;;

(defonce unique-int-counter (atom 0))

(defn unique-int
  "Returns a unique integer at every call. The current implementation starts at
  0 and counts upward."
  []
  (swap! unique-int-counter inc))

;; intervals that are reload friendly

(defonce intervals (atom {}))

(defn active-intervals
  "Returns a list of the ids of the currently running intervals that were
  created by the `set-interval` function."
  []
  (keys @intervals))

(defn clear-interval
  "Clears the interval with the specified id. If no such id exists, then nothing
  happens."
  [id]
  (assert (string? id))
  (let [i (get @intervals id)]
    (if (some? i) (do
                    (js/clearInterval i)
                    (swap! intervals dissoc id)))))

(defn clear-all-intervals
  "Clears all intervals."
  []
  (map clear-interval (active-intervals)))

(defn set-interval
  "Calls function `f` in an interval of approximately `milliseconds`
  milliseconds. If an interval with the given `id` already exists, then that
  interval is cleared and a new one is created."
  [id f milliseconds]
  (assert (string? id))
  (assert (fn? f))
  (assert (some? milliseconds))
  (clear-interval id)
  (swap! intervals assoc id (js/setInterval f milliseconds)))



(def test-str
  "all for now
  all for now
  resolved launchercmd tcp://beaglebone.local:62996
  resolved launcher tcp://beaglebone.local:49764
  removed 2 0 Machinekit Launcher on beaglebone.local _machinekit._tcp local 4
  removed 3 0 Launchercmd service on beaglebone.local pid 25044 _machinekit._tcp local 4")

(def services [:launcher, :launchercmd])

(defn parse-removed
  [line]
  (let [tokens (map (comp keyword string/lower-case) (string/split line " "))
        launchercmd (nth tokens 3)
        launcher (nth tokens 4)]
    (cond
      (= :launcher launcher) launcher
      (= :launchercmd launchercmd) launchercmd
      :else :error)))

(defn parse-resolved
  [line]
  (let [[_ service address] (string/split line " ")
        [_ _ port] (string/split address ":")]
    {(keyword service) port})

;; TODO handle all types of lines
(defn parse-lines
  [[line & lines] services]
  (if (some? line)
    (let [update (first (string/split line " "))]
      (if (= update "removed")
        (parse-lines lines (dissoc services (parse-removed line)))
        (parse-lines lines (merge services (parse-resolved line))))
    services))))

(defn parse-resolve-log
  "Takes a log of output from the resolve.py script
  and returns a dictionary of available services with their ports"
  [log]
  (let [lines (->> log string/split-lines (map string/trim))
        updates (filter #(or (string/starts-with? %1 "resolved")
                             (string/starts-with? %1 "removed"))
                        lines)]
    (parse-lines updates {})))

(defn log
  "Casts x to a str and prints it"
  [x]
  (.log js/console (str x)))

(defn- log-ssh-cmd
  "Output should be a map with the keys :exit, :err, and :out.
  :exit contains the return code
  :err contains stderr
  :out contains stdout"
  [output]
  (let [{:keys [exit err out]} output]
    (if (some? err)
      (log err))
    (if (some? out)
      (log out))))

(defn encode-buffer
  "TODO: Handle more data"
  [type]
  (let [encoded (.encode container type)
        limit (.-limit encoded)
        buffer (.-view encoded)
        sliced (map #(aget buffer %) (range 3))]
    sliced))
