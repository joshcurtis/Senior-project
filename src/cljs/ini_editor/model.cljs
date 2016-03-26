(ns ini-editor.model
  "State for the ini-editor. Codex doesn't work with `defonce` :("
  (:require
   [ini-editor.parser :as parser]
   [reagent.core :as r :refer [atom]]))


;; hashmap: [source path] -> {:ini parsed-ini
;;                            :expanded? #{section-strings}
(defonce inis (atom {}))
(defonce selected-id (atom nil))

;; Convenient helpers

(defn ini-str
  "Convert the current model into an INI for MachineKit to use."
  []
  (parser/ini-to-str (get-in @inis [@selected-id :ini])))
