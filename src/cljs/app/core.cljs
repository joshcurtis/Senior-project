(ns app.core
  "Mounts the application onto the `div` with the id `"app"`."
  (:require
   [model.core :as model]
   [controller.core]
   [view.core]
   [cljsjs.bootstrap]
   [app.devtools-setup]
   [view.topbar]
   [widgets.core :as widgets]
   [utils.core :as utils]
   [viz.core :as viz]
   [reagent.core :as r :refer [atom]]))

(defn app
  "Reagent component which describes the app. It is a tab bar followed by the
  contents of that tab."
  [props]
  (let [tab @(r/cursor model/state [:tab])
        tab-labels @(r/cursor model/state [:tab-labels])]
    [:div.app {}
     [view.topbar/render-topbar {}]
     [widgets/tabs {:labels tab-labels
                    :id-prefix "tab-navigation-"
                    :selected tab
                    :on-change #(controller.core/set-tab! %1)}]
     [view.core/app-view]]))

(defn ^:export start
  "Renders the application onto the DOM element \"app\""
  []
  (r/render-component [app {}]
                      (.getElementById js/document "app")))

(start)
