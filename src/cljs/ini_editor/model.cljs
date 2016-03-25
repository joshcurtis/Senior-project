(ns ini-editor.model
  "State for the ini-editor. Codex doesn't work with `defonce` :("
  (:require
   [ini-editor.parser :as parser]
   [reagent.core :as r :refer [atom]]))


;; From parsed ini
(defonce ini (atom {}))

;; #{section-string}
(defonce expanded? (atom #{}))

;; bool
(defonce loaded? (atom false))

;; Convenient helpers

(defn save-model
  "Takes a snapshot of the model for use with `load-model!`"
  []
  {:ini @ini
   :expanded? @expanded?})

(defn load-model!
  "Loads a snapshot created with `save-model`"
  [snapshot]
  (let [ini-snapshot (:ini snapshot)
        exp-snapshot (:expanded? snapshot)]
    (assert (some? ini-snapshot))
    (assert (some? exp-snapshot))
    (reset! ini ini-snapshot)
    (reset! loaded? true)
    (reset! expanded? exp-snapshot)))

(defn unload-model!
  "Resets the model to its default state."
  []
  (reset! loaded? false)
  (reset! ini {})
  (reset! expanded? #{}))

(defn sections
  "Retrieves the sections in the current ini."
  []
  (:section-order @ini))

(defn ini-str
  "Convert the current model into an INI for MachineKit to use."
  []
  (parser/ini-to-str @ini))
