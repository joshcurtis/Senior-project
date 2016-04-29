(ns app.core
  ""
  (:require
   [model.core :as model]
   [cljsjs.bootstrap]
   [app.devtools-setup]
   [app.view]
   [app.topbar]
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [three.core :as three]
   [reagent.core :as r :refer [atom]]))

(defn app
  "Reagent component which describes the app. It is a tab bar followed by the
  contents of that tab. Here are the locations of the contents:
  \"Home\" - `machine-conf.core/home`
  \"Remote\" - `remote-manager.core/view`
  \"INI\" - `ini-editor.core/view`"
  [props]
  (let [tab @(r/cursor model/state [:tab])
        tab-labels @(r/cursor model/state [:tab-labels])]
    [:div.app {}
     [app.topbar/render-topbar {}]
     [widgets/tabs {:labels tab-labels
                    :id-prefix "tab-navigation-"
                    :selected tab
                    :on-change #(app.view/set-tab! %1)}]
     [app.view/render-contents]]))

(defn ^:export start
  "Renders the application onto the DOM element \"app\""
  []
  (r/render-component [app {}]
                      (.getElementById js/document "app")))

(start)
