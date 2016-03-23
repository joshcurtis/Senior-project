(ns ini-editor.controller
  "Actions for editing the ini files."
  (:require
   [ini-editor.model :as model]
   [ini-editor.parser :as parser]
   [machine-conf.utils :as utils]
   [reagent.core :as r :refer [atom]]))

(enable-console-print!)

;; expanding/collapsing

(defn- is-important?
  "Helper function for determining if a section is important, given it's
  metadata. This is done by checking if it has the \"unimportant\" tag."
  [metadata]
  (let [tags (get metadata :tags #{})]
    (assert (set? tags))
    (not (contains? tags "unimportant"))))

(defn expand-all!
  "All sections are added to the expanded set."
  []
  (reset! model/expanded? (set (keys @model/values))))

(defn toggle-expanded!
  "If the given section (string) is in the expanded set, then it is removed. If
  it is not in the expanded set, then it is added."
  [section]
  (assert (string? section))
  (swap! model/expanded? utils/toggle-membership section))

(defn expand-important!
  "Every section that is important is added to the expanded set. An important
  section is a section whose metadata does *not* include the \"unimportant\" in
  it's tags."
  []
  (let [meta @model/section-metadata
        important (filter #(-> %1 second is-important?) meta)
        important-set (set (map first important))]
    (swap! model/expanded?
           clojure.set/union
           important-set)))

;; editing

(defn set-ini-value!
  "The value in the provided section/key is set to the new value"
  [section key value]
  (swap! model/values assoc-in [section key] value))

(defn load-str!
  "The given ini string representation is loaded for editing. The model is
  updated to represent the new ini and ini-editor.controller/expand-important!
  is called."
  [s]
  (let [parsed (parser/parse-ini s)]
    (reset! model/key-metadata (:key-metadata parsed))
    (reset! model/key-order (:key-order parsed))
    (reset! model/section-metadata (:section-metadata parsed))
    (reset! model/section-order (:section-order parsed))
    (reset! model/values (:values parsed))
    (expand-important!)))
