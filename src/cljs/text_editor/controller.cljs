(ns text-editor.controller
  "Actions for editing the ini files."
  (:require
   [app.store :as store]
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
  (swap! store/state --load-text id text))

(defn set-selected-id!
  [id]
  (swap! store/state assoc
         :selected-text-id id))

(defn- --close-selected
  [state]
  (let []
    (assoc state :selected-text-id nil)))

(defn close-selected!
  "Closes the text that is being edited."
  []
  (swap! store/state --close-selected))

(defn filename
  []
  (-> @store/state :selected-text-id second utils/fname-from-path))

;; editing

(defn- --change-current-text
  [state new-text]
  (let [{:keys [selected-text-id]} state]
    (assoc-in state [:texts selected-text-id] new-text)))

(defn change-current-text!
  "Sets the currently selected text value."
  [new-text]
  {:pre [(string? new-text)]}
  (swap! store/state --change-current-text new-text))
