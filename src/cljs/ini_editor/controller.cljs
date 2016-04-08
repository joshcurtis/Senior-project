(ns ini-editor.controller
  "Actions for editing the ini files."
  (:require
   [app.store :as store]
   [ini-editor.parser :as parser]
   [utils.core :as utils]
   [clojure.string :as string]
   [reagent.core :as r :refer [atom]]))

(enable-console-print!)

;; topbar

(defn- --load-str
  [state id s]
  (let [ini (parser/parse-ini s)
        s-metas (:section-metadata ini)
        is-important? #(not (get %1 :unimportant false))
        imp (filter #(-> %1 second is-important?)
                    s-metas)
        imp-set (set (map first imp))]
    (-> state
        (assoc-in [:inis id :ini] ini)
        (assoc-in [:inis id :expanded?] imp-set)
        (assoc :selected-ini-id id))))

(defn load-str!
  "The given ini string representation is loaded for editing. The model is
  updated to represent the new ini And the selected id is changed to the new
  str. ini-editor.controller/expand-important! is called."
  [id s]
  (swap! store/state --load-str id s))

(defn ini-str
  []
  (store/ini-str))

(defn- --close-selected
  [state]
  (let [{:keys [selected-ini-id inis]} state]
    (assoc state
           :selected-ini-id nil
           :inis (dissoc inis selected-ini-id))))

(defn close-selected!
  "Closes the currently selected ini."
  []
  (swap! store/state --close-selected))

(defn filename
  "Gets the filename for the currently selected ini. This does not include the
  path."
  []
  (utils/fname-from-path (-> @store/state :selected-ini-id second)))

(defn filename-filter
  "Given a filename, it returns true if the filename has the right mimetype for
  an ini, ends in .ini. False is returned if the mimetype does not match."
  [fname]
  (string/ends-with? fname ".ini"))

;; expanding/collapsing and viewing

(defn- --toggle-expanded
  [state section]
  (let [{:keys [inis selected-ini-id]} state]
    (update-in state
               [:inis selected-ini-id :expanded?]
               utils/toggle-membership section)))

(defn toggle-expanded!
  "Toggles the given section between expanded and collapsed."
  [section]
  {:pre [(string? section)]}
  (swap! store/state --toggle-expanded section))

(defn set-selected-id!
  [id]
  {:pre [(or (vector? id) (nil? id))]}
  (swap! store/state assoc
         :selected-ini-id id))

;; editing

(defn- --set-ini-value
  [state section key value]
  (let [{:keys [inis selected-ini-id]} state]
    (assoc-in state
              [:inis selected-ini-id :ini :values section key]
              value)))

(defn set-ini-value!
  "The value in the provided section/key is set to value."
  [section key value]
  {:pre [(string? section) (string? key) (string? value)]}
  (swap! store/state --set-ini-value section key value))
