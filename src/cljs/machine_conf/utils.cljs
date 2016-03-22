(ns machine-conf.utils
  ""
  (:require
   [clojure.string :as string]))

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
  [text filename]
  (let [blob (js/Blob. #js [text] #js {"type" "text/plain;charset=utf-8"})]
    (js/saveAs blob filename)))

(defn toggle-membership
  [s v]
  (assert (set? s))
  (if (contains? s v)
    (clojure.set/difference s #{v})
    (clojure.set/union s #{v})))
