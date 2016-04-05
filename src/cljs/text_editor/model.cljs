(ns text-editor.model
  "State for the ini-editor. Codex doesn't work with `defonce` :("
  (:require
   [reagent.core :as r :refer [atom]]))


;; hashmap: [source path] -> {:ini parsed-ini
;;                            :expanded? #{section-strings}
;; hashmap: [source path] -> string
(defonce texts (atom {[:local "scratch.txt"] ""}))
(defonce selected-id (atom [:local "scratch.txt"]))

;; Convenient helpers

(defn text-str
  "Convert the current model into an string. nil is returned if there is no text
  that is currently being edited."
  []
  (get @texts @selected-id))
