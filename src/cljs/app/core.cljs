(ns app.core
  ""
  (:require
   [app.actions]
   [ini-editor.core :as ini-editor]
   [remote-manager.core :as remote-manager]
   [server-interop.core :as server-interop]
   [text-editor.core :as text-editor]
   [utils.widgets :as widgets]
   [utils.navbar :as navbar]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))


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

(defn nil-navbar
  "A navbar with nothing in it. Use it for placeholding"
  [{:keys [title]}]
  [navbar/navbar {:title (or title "")
                  :elements []}])


(defonce text (atom ""))
(defn text-editor
  [props]
  [:pre
   [:textarea {:value @text
               :rows (->> @text (re-seq #"\n") count inc)
               :style {:width "100%"
                       :height "100%"}
               :on-change #(reset! text (-> %1 .-target .-value))}]])

(def contents-map {"Home" [home {}]
                   "Remote" [remote-manager/contents {}]
                   "INI" [ini-editor/contents {}]
                   "Text" [text-editor/contents {}]})

(def topbar-map {"Home" [nil-navbar {:title "Home"}]
                 "Remote" [nil-navbar {:title "Remote"}]
                 "INI" [ini-editor/topbar {}]
                 "Text" [text-editor/topbar {}]})

(defn app
  "Reagent component which describes the app. It is a tab bar followed by the
  contents of that tab. Here are the locations of the contents:
  \"Home\" - `machine-conf.core/home`
  \"Remote\" - `remote-manager.core/view`
  \"INI\" - `ini-editor.core/view`"
  [props]
  (let [tab (:tab @app.actions/app-state)]
    [:div.app {}
     (get topbar-map tab [nil-navbar {}])
     [widgets/tabs {:labels ["Home" "Remote" "INI" "Text"]
                    :selected tab
                    :on-change #(app.actions/set-tab! %1)}]
     [:div.tab-content.panel.panel-default {}
      [:div.panel-body
       [:div.tab-pane.active {}
        (get contents-map tab [:div "Unknown Tab"])]]]]))

(defn ^:export start
  "Renders the application onto the DOM element \"app\""
  []
  (r/render-component
   [app {}]
   (.getElementById js/document "app")))
