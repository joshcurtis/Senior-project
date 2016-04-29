(ns controller.text-editor
  "Actions for editing the text files."
  (:require
   [model.core :as model]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(enable-console-print!)

;; loading/saving

(defn- --load-text
  [state id text]
  (let [id (utils/gen-unique-id (-> state :texts keys set) id)]
    (-> state
        (assoc-in [:texts id] text)
        (assoc :selected-text-id id))))

(defn load-text!
  "Loads the given text for editing."
  [id text]
  {:pre [(vector? id) (string? text)]}
  (swap! model/state --load-text id text))

(defn set-selected-id!
  [id]
  (swap! model/state assoc
         :selected-text-id id))

(defn- --close-selected
  [state]
  (let []
    (assoc state :selected-text-id nil)))

(defn close-selected!
  "Closes the text that is being edited."
  []
  (swap! model/state --close-selected))

(defn filename
  []
  (-> @model/state :selected-text-id second utils/fname-from-path))

;; editing

(defn- --change-current-text
  [state new-text]
  (let [{:keys [selected-text-id]} state]
    (assoc-in state [:texts selected-text-id] new-text)))

(defn change-current-text!
  "Sets the currently selected text value."
  [new-text]
  {:pre [(string? new-text)]}
  (swap! model/state --change-current-text new-text))
