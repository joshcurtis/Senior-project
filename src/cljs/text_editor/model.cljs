(ns text-editor.model
  "State for the ini-editor. Codex doesn't work with `defonce` :("
  (:require
   [reagent.core :as r :refer [atom]]))


;; hashmap: [source path] -> {:ini parsed-ini
;;                            :expanded? #{section-strings}
;; hashmap: [source path] -> string
(defonce texts (atom {}))
(defonce selected-id (atom nil))

;; Convenient helpers

(defn text-str
  "Convert the current model into an string."
  []
  (get @texts @selected-id))
