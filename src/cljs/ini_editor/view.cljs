(ns ini-editor.view
  "Provides the view for the INI editor."
  (:require
   [ini-editor.controller :as controller]
   [ini-editor.model :as model]
   [ini-editor.parser :as parser]
   [utils.core :as utils]
   [utils.widgets :as widgets]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(defn- ini-key
  [props]
  (let [{:keys [section key metadata value]} props
        comments (:comments metadata)
        type (or (:type metadata) "text")
        disabled false]
    [:div.ini-key.form-group {}
     [:label.control-label {:title comments} key]
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
                metadata
                values
                expanded?]} props
        comments (:comments metadata)]
    [:div.ini-section.panel.panel-default {}
     [:div.panel-heading {}
      [:legend.panel-title {:stlye {:font-weight "bold"}
                            :title comments
                            :on-click #(controller/toggle-expanded! section)}
       section]]
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
  # Props - same hashmap are present in `ini-editor/model`.
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
  "Renders a component for editing the current ini, if there is one. See
  ini-editor.view/ini-editor-active for the props."
  [props]
  (let [{:keys [selected-id]} props]
    (if (some? selected-id) [ini-editor-active props] [ini-editor-inactive props])))

(defn- navbar
  [{:keys [selected-id]}]
  (let [target-fname (utils/fname-from-path (second selected-id))]
    [:nav.navbar.navbar-default
     [:div.container-fluid
      [:div.navbar-header
       [:button.navbar-toggle.collapsed {:type "button"
                                         :data-toggle "collapse"
                                         :data-target "#ini-editor-navbar"}
        [:span.sr-only "Toggle Navigation"]
        [:span.icon-bar]
        [:span.icon-bar]
        [:span.icon-bar]]
       [:a.navbar-brand "INI"]]
      [:div.collapse.navbar-collapse {:id "ini-editor-navbar"}
       [:ul.nav.navbar-nav
        [:li.dropdown
         [:a.dropdown-toggle {:data-toggle "dropdown"
                              :role "button"
                              :aria-expanded "false"} "File" [:span.caret]]
         [:ul.dropdown-menu {:role "menu"}
          [:li [:a [widgets/file-input
                    {:id "file-input"
                     :file-types ".ini"
                     :element "Open"
                     :on-change (fn [file-list]
                                  (let [file (first file-list)
                                        ini-id [:local (.-name file)]]
                                    (if (some? file)
                                      (utils/read-file file
                                                       #(controller/load-str!
                                                         ini-id
                                                         %1)))))}]]]
          [:li [:a [widgets/file-save {:element "Save"
                                       :filename target-fname
                                       :str-func model/ini-str}]]]
          ]]
      ;; [:li [:a "Filler"]]
        ]]]]))

(defn menubar
  "Renders a menubar for misc. actions such as loading and saving a file."
  [props]
  (let [{:keys [selected-id all-ids]} props
        [source fname] selected-id
        path (concat [[:a (str source)]] (string/split fname \/))]
    [:div
     [navbar {:selected-id selected-id}]
     (let [all-ids all-ids]
       [widgets/pagination
        {:labels (map (fn [[source fname]] [(utils/fname-from-path fname) [source fname]])
                      all-ids)
         :selected selected-id
         :on-change controller/set-selected-id!}])
     [:span [widgets/file-path {:path path}]]
     ]))
