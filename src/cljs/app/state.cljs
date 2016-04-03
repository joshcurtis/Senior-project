(ns app.state
  ""
  (:require
   [ini-editor.core]
   [remote-manager.core]
   [text-editor.core]
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
