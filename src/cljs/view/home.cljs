(ns view.home
  "Provides the view for the home tab. The home tab has some links and can
  change the theme."
  (:require
   [model.core :as model]
   [controller.core]
   [widgets.core :as widgets]
   [reagent.core :as r :refer [atom]]))

(def mk-link "https://github.com/machinekit/machinekit")
(def mk-documentation-link "https://github.com/machinekit/machinekit-docs/blob/master/machinekit-documentation/getting-started/getting-started-platform.asciidoc")
(def themes ["flatly"
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
             "united"])

;; contents
(defn contents
  "reagent-component for home tab."
  [props]
  (let [tab (r/cursor model/state [:tab])
        tab-labels (r/cursor model/state [:tab-labels])
        current-theme (r/cursor model/state [:current-theme])]
    [:div
     [:h1 [:a {:href mk-link
               :target "_blank"} "MachineKit"]]
     [:div "MachineKit is a platform for machine control applications."]
     [:h2 [:a {:href mk-documentation-link
               :target "_blank"} "Installation"]]
     [:h3 {:style {:marginTop "51px"}} "Select a Theme"]
     [widgets/dropdown-input {:disabled false
                              :options themes
                              :value @current-theme
                              :on-change controller.core/change-theme!}]]))
