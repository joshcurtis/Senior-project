(ns ini-editor.model
  "State for the ini-editor. Codex doesn't work with `defonce` :("
  (:require
   [ini-editor.parser :as parser]
   [reagent.core :as r :refer [atom]]))


;; From parsed ini

;; section-string -> key-string -> hashmap
(defonce key-metadata (atom {}))

;; section-string -> [key-string]
(defonce key-order (atom {}))

;; section-string -> hashmap
(defonce section-metadata (atom {}))

;; [section-string]
(defonce section-order (atom []))

;; section-string -> key-string -> string-value | [string-value]
(defonce values (atom {}))


;; Other gui

;; #{section-string}
(defonce expanded? (atom #{}))

;; Convenient helpers

(defn ini-str
  "Convert the current model into an INI for MachineKit to use."
  []
  (parser/ini-to-str {:key-metadata @key-metadata
                      :key-order @key-order
                      :section-metadata @section-metadata
                      :values @values}))
