(ns utils.core
  "Generic utility functions."
  (:require
   [clojure.set]
   [clojure.string :as string]))

(defn alert
  "Shows an alert box with the provided arguments displayed as strings."
  ([arg] (js/alert (str arg)))
  ([& args] (js/alert (str args))))

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
