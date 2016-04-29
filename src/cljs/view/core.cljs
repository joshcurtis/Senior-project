(ns view.core
  (:require
   [model.core :as model]
   [controller.core]
   [view.ini-editor]
   [view.monitor]
   [view.remote]
   [view.text-editor]
   [widgets.core :as widgets]
   [reagent.core :as r]))

(def remote view.remote/contents)
(def monitor view.monitor/contents)
(def ini view.ini-editor/contents)
(def text view.text-editor/contents)

;; contents
(defn home
  "reagent-component for home tab."
  [props]
  (let [tab (r/cursor model/state [:tab])
        tab-labels (r/cursor model/state [:tab-labels])
        current-theme (r/cursor model/state [:current-theme])]
    [:div
     [:h1 [:a {:href "https://github.com/machinekit/machinekit"
               :target "_blank"} "MachineKit"]]
     [:div "MachineKit is a platform for machine control applications."]
     [:h2 [:a {:href "https://github.com/machinekit/machinekit-docs/blob/master/machinekit-documentation/getting-started/getting-started-platform.asciidoc"
               :target "_blank"} "Installation"]]
     [:h3 {:style {:marginTop "51px"}} "Select a Theme"]
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
                              :value @current-theme
                              :on-change controller.core/change-theme!}]]))

(defn app-view
  ""
  []
  (let [tab @(r/cursor model/state [:tab])]
    [:div.tab.content.panel.panel-default
      [:div.panel-body
        [:div.tab-pane.active {}
          (case tab
            "Home" [home {}]
            "Remote" [remote {}]
            "Monitor" [monitor {}]
            "INI" [ini {}]
            "Text" [text {}]
          )]]]))
