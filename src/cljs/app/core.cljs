(ns app.core
  ""
  (:require
   [ini-editor.core :as ini-editor]
   [remote-manager.core :as remote-manager]
   [server-interop.core :as server-interop]
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))


(defonce app-state (atom {:tab "Home"}))
(defn set-tab! [label] (swap! app-state assoc :tab label))

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

(defonce text (atom ""))

(defn text-editor
  [props]
  [:pre
   [:textarea {:value @text
               :rows (->> @text (re-seq #"\n") count inc)
               :style {:width "100%"
                       :height "100%"}
               :on-change #(reset! text (-> %1 .-target .-value))}]])

(defn app
  "Reagent component which describes the app. It is a tab bar followed by the
  contents of that tab. Here are the locations of the contents:
  \"Home\" - `machine-conf.core/home`
  \"Remote\" - `remote-manager.core/view`
  \"INI\" - `ini-editor.core/view`"
  [props]
  (let [tab (:tab @app-state)]
    [:div.app {}
     [widgets/tabs {:labels ["Home" "Remote" "INI" "Text"]
                    :selected tab
                    :on-change #(set-tab! %1)}]
     [:div.tab-content.panel.panel-default {}
      [:div.panel-body
       [:div.tab-pane.active {}
        (cond
          (= tab "Home") [home {}]
          (= tab "Remote") [remote-manager/view {}]
          (= tab "INI") [ini-editor/view {}]
          (= tab "Text") [text-editor {}]
          :else [:div {} "Unknown tab"])]]]]))

(defn ^:export start
  "Renders the application onto the DOM element \"app\""
  []
  (r/render-component
   [app {}]
   (.getElementById js/document "app")))
