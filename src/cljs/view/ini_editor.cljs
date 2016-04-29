(ns view.ini-editor
  "Provides the view for the INI editor."
  (:require
   [model.core :as model]
   [controller.ini-editor :as controller]
   [utils.ini :as parser]
   [utils.core :as utils]
   [utils.widgets :as widgets]
   [utils.navbar :as navbar]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(defn- ini-key
  [props]
  (let [{:keys [section key metadata value]} props
        comments (:comments metadata)
        type (get metadata :type "text")
        disabled (get metadata :disabled false)]
    [:div.ini-key.form-group {:style {:margin-bottom "4rem"}}
     [:label.control-label {:title comments} key]
     (if (some? comments) [:div.alert.alert-info {:style
                                                  {:margin-bottom "1rem"}}
                           [widgets/endlines-to-divs comments]])
     (cond
       (= type "text")
       [:input.form-control {:disabled disabled
                             :on-change #(->>
                                          %1
                                          .-target
                                          .-value
                                          (controller/set-ini-value! section key))
                             :value value
                             :type "text"}]

       (= type "options") [widgets/dropdown-input
                           {:disabled disabled
                            :options (:options metadata)
                            :on-change #(controller/set-ini-value! section key %1)
                            :value value}]

       (= type "multiline") [widgets/list-input
                             {:disabled disabled
                              :on-change #(controller/set-ini-value! section key %1)
                              :value value
                              :default-value ""}]

       :else
       [:span {} (str "Unknown:" props)])]))

(defn- ini-section
  [props]
  (let [{:keys [section
                key-metadata
                key-order
                metadata values
                expanded?]} props
        comments (:comments metadata)]
    [:div.ini-section.panel.panel-default {}
     [:div.panel-heading {}
      [:legend.panel-title {:style {:cursor "pointer" :font-weight "bold"}
                            :title comments
                            :on-click #(controller/toggle-expanded! section)}
       section]]
     (if (and expanded? (some? comments))
       [:div.alert.alert-info [widgets/endlines-to-divs comments]])
     [:fieldset {}
      (if expanded?
        [:div.panel-body.form-group {}
         (map (fn [k] [ini-key {:section section
                                :key k
                                :metadata (get key-metadata k)
                                :value (get values k)}])
              key-order)])]]))

(defn- ini-editor-active
  "Renders a component for editing the current ini.
  # Props - same hashmaps are present in `(:inis @store/state)`.
  :key-metadata
  :key-order
  :section-metadata
  :section-order
  :values
  :expanded?
  :selected-id"
  [props]
  (let [{:keys [key-metadata
                key-order
                section-metadata
                section-order
                values
                expanded?
                selected-id]} props
        [source fname] selected-id
        path (concat [[:a (str source)]] (string/split fname \/))]
    (assert (some? key-metadata))
    (assert (some? key-order))
    (assert (some? section-metadata))
    (assert (some? section-order))
    (assert (some? values))
    (assert (some? expanded?))
    (assert (some? selected-id))
    [:div.ini-editor.panel.pane-default {}
     (map (fn [section] [ini-section {:key section ;; for react/reagent
                                      :section section
                                      :key-metadata (get key-metadata section)
                                      :key-order (get key-order section)
                                      :metadata (get section-metadata section)
                                      :values (get values section)
                                      :expanded? (contains? expanded? section)}])
          section-order)]))

(defn- ini-editor-inactive
  []
  [:div.alert.alert-dismissible.alert-warning
   [:h4 "Nothing Loaded"]
   [:p "No configuration has been loaded, open a file or select a remote file."]])

(defn ini-editor
  [props]
  (let [{:keys [selected-id all-ids]} props]
    [:div
     [widgets/infosection {:selected-id selected-id
                           :all-ids all-ids
                           :on-change-id controller/set-selected-id!}]
     (if (some? selected-id) [ini-editor-active props] [ini-editor-inactive props])]))

(def topbar-actions {"open" controller/load-str!
                     "save" controller/ini-str
                     "close" controller/close-selected!
                     "filename" controller/filename
                     "filename-filter" #(string/ends-with? %1 ".ini")})

(defn contents
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [inis @(r/cursor model/state [:inis])
        selected-ini-id @(r/cursor model/state [:selected-ini-id])
        all-ids (keys inis)
        model-ini (get inis selected-ini-id)]
    [ini-editor (merge (:ini model-ini)
                       {:expanded? (:expanded? model-ini)
                        :all-ids all-ids
                        :selected-id selected-ini-id})]))

(defn ini-as-str
  "Convert the ini data that is currently in the ini-editor/model into an ini
  string compatible with machinekit."
  []
  (model/ini-str))
