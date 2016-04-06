(ns text-editor.controller
  "Actions for editing the ini files."
  (:require
   [text-editor.model :as model]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(enable-console-print!)

;; loading/saving

(defn load-text!
  "Loads the given text for editing."
  [id text]
  (assert (some? id))
  (assert (string? text))
  (swap! model/texts assoc
         id text)
  (reset! model/selected-id id))

(defn set-selected-id!
  [id]
  (reset! model/selected-id id))

(defn close-selected!
  "Closes the text that is being edited."
  []
  (let [selected-id @model/selected-id]
    (reset! model/selected-id nil)
    (reset! model/selected-id (-> (swap! model/texts dissoc selected-id)
                                  keys
                                  first))))

(defn filename [] (utils/fname-from-path (second @model/selected-id)))

;; editing

(defn change-current-text
  "Sets the currently selected text value."
  [new-text]
  (assert (string? new-text))
  (swap! model/texts assoc
         @model/selected-id new-text))
