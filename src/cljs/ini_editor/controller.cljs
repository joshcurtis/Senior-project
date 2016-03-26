(ns ini-editor.controller
  "Actions for editing the ini files."
  (:require
   [ini-editor.model :as model]
   [ini-editor.parser :as parser]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(enable-console-print!)

;; expanding/collapsing

(defn- is-important?
  "Helper function for determining if a section is important, given its
  metadata. This is done by checking if :tag (which is a set) contains
  \"unimportant\"."
  [metadata]
  (let [tags (get metadata :tags #{})]
    (assert (set? tags))
    (not (contains? tags "unimportant"))))

(defn- get-sections
  "Helper function for getting the sections."
  []
  (get-in @model/inis [@model/selected-id :ini :section-order]))

(defn expand-all!
  "All sections are added to the expanded set."
  []
  (swap! model/inis assoc-in
         [@model/selected-id :expanded?] (set (get-sections))))

(defn toggle-expanded!
  "If the given section (string) is in the expanded set, then it is removed. If
  it is not in the expanded set, then it is added."
  [section]
  (assert (string? section))
  (swap! model/inis update-in
         [@model/selected-id :expanded?] utils/toggle-membership section))

(defn expand-important!
  "Every section that is important is added to the expanded set. An important
  section is a section whose metadata does *not* include the \"unimportant\" in
  it's tags."
  []
  (let [selected-id @model/selected-id
        meta (get-in @model/inis [selected-id :ini :section-metadata])
        important (filter #(-> %1 second is-important?) meta)
        important-set (set (map first important))]
    (swap! model/inis update-in
           [selected-id :expanded?] clojure.set/union important-set)))

;; editing

(defn set-ini-value!
  "The value in the provided section/key is set to the new value"
  [section key value]
  (assert (string? section))
  (assert (string? key))
  (assert (some? value))
  (swap! model/inis assoc-in
         [@model/selected-id :ini :values section key] value))

(defn load-str!
  "The given ini string representation is loaded for editing. The model is
  updated to represent the new ini And the selected id is changed to the new
  str. ini-editor.controller/expand-important! is called."
  [id s]
  (assert (some? id))
  (assert (string? s))
  (let [parsed (parser/parse-ini s)]
    (swap! model/inis assoc
           id {:ini parsed :expanded? #{}})
    (reset! model/selected-id id)
    (expand-important!)))

(defn save-str!
  "Saves the given ini into its string representation on the users local
  machine."
  []
  (assert "Use `widgets/file-save` instead")
  nil)
