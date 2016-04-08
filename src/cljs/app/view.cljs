(ns app.view
  ""
  (:require
   [app.store :as store]
   [ini-editor.core]
   [remote-manager.core]
   [monitor.core]
   [text-editor.core]
   [utils.core :as utils]
   [utils.navbar :as navbar]
   [utils.widgets :as widgets]
   [reagent.core :as r :refer [atom]]))

(defn set-tab!
  [label]
  (swap! store/state assoc :tab label))

(defn change-theme! [theme]
  (swap! store/state assoc :current-theme theme)
  (js/changeStyle (str "css/bootstrap-" theme ".css")))

;; contents
(defn home
  "reagent-component for home tab."
  [props]
  (let [{:keys [tab tab-labels current-theme]} @store/state]
    [:div
     [:h1 [:a {:href "https://github.com/machinekit/machinekit"
               :target "_blank"} "MachineKit"]]
     [:div "MachineKit is a platform for machine control applications."]
     [:h2 [:a
           {:href
            "https://github.com/machinekit/machinekit-docs/blob/master/machinekit-documentation/getting-started/getting-started-platform.asciidoc"
            :target "_blank"}
           "Installation"]]
     [:h2 "Theme"]
     [widgets/dropdown-input {:disabled false
                              :options ["flatly"
                                        "cerulean"
                                        "paper"
                                        "slate"
                                        "yeti"
                                        "cosmo"
                                        "journal"
                                        "readable"
                                        "spacelab"
                                        "cyborg"
                                        "lumen"
                                        "sandstone"
                                        "superhero"
                                        "darkly"
                                        "metro"
                                        "simplex"
                                        "united"]
                              :value current-theme
                              :on-change change-theme!}]]))

(def contents-map {"Home" [home {}]
                   "Remote" [remote-manager.core/contents {}]
                   "Monitor" [monitor.core/contents {}]
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
  (let [tab (:tab @store/state)]
    [:div.tab.content.panel.panel-default
     [:div.panel-body
      [:div.tab-pane.active {}
       (get contents-map tab [:div "Unknown Tab"])]]]))
