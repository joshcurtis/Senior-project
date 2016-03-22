(ns ini-editor.controller
  "Actions for ini-editing"
  (:require
   [ini-editor.model :as model]
   [ini-editor.parser :as parser]
   [machine-conf.utils :as utils]
   [reagent.core :as r :refer [atom]]))

(enable-console-print!)

;; expanding/collapsing

(defn- is-important?
  [metadata]
  (let [tags (get metadata :tags #{})]
    (assert (set? tags))
    (not (contains? tags "unimportant"))))

(defn expand-all!
  []
  (reset! model/expanded? (set (keys @model/values))))

(defn toggle-expanded!
  [section]
  (assert (string? section))
  (swap! model/expanded? utils/toggle-membership section))

(defn expand-important!
  []
  (let [meta @model/section-metadata
        important (filter #(-> %1 second is-important?) meta)
        important-set (set (map first important))]
    (swap! model/expanded?
           clojure.set/union
           important-set)))

;; editing

(defn set-value!
  [section key value]
  (swap! model/values assoc-in [section key] value))

(defn load-str!
  [s]
  (let [parsed (parser/parse-ini s)]
    (reset! model/key-metadata (:key-metadata parsed))
    (reset! model/key-order (:key-order parsed))
    (reset! model/section-metadata (:section-metadata parsed))
    (reset! model/section-order (:section-order parsed))
    (reset! model/values (:values parsed))
    (expand-important!)))
