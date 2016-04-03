(ns app.state
  ""
  (:require
   [ini-editor.core]
   [remote-manager.core]
   [text-editor.core]
   [utils.core :as utils]
   [utils.navbar :as navbar]
   [utils.widgets :as widgets]
   [reagent.core :as r :refer [atom]]))

(defonce app-state (atom {:tab "Home"
                          :tab-labels ["Home" "Remote" "INI" "Text"]}))

(defn set-tab! [label]
  (swap! app-state assoc :tab label))

;; contents

(defn home
  "reagent-component for home tab."
  [props]
  [:div
   [:h1 [:a {:href "https://github.com/machinekit/machinekit"
             :target "_blank"} "MachineKit"]]
   [:div "MachineKit is a platform for machine control applications."]
   [:h2 [:a
         {:href
          "https://github.com/machinekit/machinekit-docs/blob/master/machinekit-documentation/getting-started/getting-started-platform.asciidoc"
          :target "_blank"}
         "Installation"]]])

(def contents-map {"Home" [home {}]
                   "Remote" [remote-manager.core/contents {}]
                   "INI" [ini-editor.core/contents {}]
                   "Text" [text-editor.core/contents {}]})

(defonce text (atom ""))
(defn text-editor
  [props]
  [:pre
   [:textarea {:value @text
               :rows (->> @text (re-seq #"\n") count inc)
               :style {:width "100%"
                       :height "100%"}
               :on-change #(reset! text (-> %1 .-target .-value))}]])

(defn render-contents
  ""
  []
  (let [app-state @app-state
        tab (:tab app-state)]
    [:div.tab.content.panel.panel-default
     [:div.panel-body
      [:div.tab-pane.active {}
       (get contents-map tab [:div "Unknown Tab"])]]]))

;; topbar

(defn nil-navbar
  "A navbar with nothing in it. Use it for placeholding"
  [{:keys [title]}]
  [navbar/navbar {:title (or title "")
                  :elements []}])

(defn alert-invalid-action
  []
  (js/alert "Invalid Action")
  "")

;; topbar-actions may define these functions:
;; "save" -> returns a string
;; "filename" -> returns a string
;; "open" -> takes an id and a string
(def topbar-actions-map {"HOME" nil
                         "Remote" nil
                         "INI" ini-editor.core/topbar-actions
                         "Text" nil})

(defn current-topbar-actions
  []
  (get topbar-actions-map (:tab @app-state)))

(defn topbar-action-open
  ""
  [id string]
  (let [f (get (current-topbar-actions) "open")]
    (if (fn? f)
      (f id string)
      (alert-invalid-action))))

(defn topbar-action-save
  "Returns a string."
  []
  ((get (current-topbar-actions) "save" alert-invalid-action)))

(defn topbar-action-filename
  "Returns a string."
  []
  ((get (current-topbar-actions) "filename" alert-invalid-action)))

(defn topbar-file-menu
  ""
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        open-el [widgets/file-input {:id "file-input"
                                     :file-types nil
                                     :element "Open"
                                     :on-change (fn [file-list]
                                                  (let [file (first file-list)
                                                        f-id [:local (.-name file)]]
                                                    (if (some? file)
                                                      (utils/read-file file
                                                                       #(topbar-action-open f-id
                                                                                            %1)))))}]
        download-el [widgets/file-save {:element "Download"
                                        :filename topbar-action-filename
                                        :str-func topbar-action-save}]
        dropdowns [
                   (if (contains? valid-options "save") open-el)
                   (if (and (contains? valid-options "save")
                            (contains? valid-options "filename"))
                     download-el)
                   ]
        should-show? (some some? dropdowns) ; only show if there is a non-nil in dropdowns
        ]
    (if should-show?
      [navbar/navbar-dropdown {:title "File"
                               :key "file"
                               :labels dropdowns}])))

(defn render-topbar
  ""
  []
  (let [app-state @app-state
        tab (:tab app-state)]
    [navbar/navbar {:title tab
                    :elements [(topbar-file-menu)]}]))
