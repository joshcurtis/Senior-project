(ns app.core
  ""
  (:require
   [app.devtools-setup]
   [app.store :as store]
   [app.view]
   [app.topbar]
   [ini-editor.core :as ini-editor]
   [remote-manager.core :as remote-manager]
   [server-interop.core :as server-interop]
   [text-editor.core :as text-editor]
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(defn app
  "Reagent component which describes the app. It is a tab bar followed by the
  contents of that tab. Here are the locations of the contents:
  \"Home\" - `machine-conf.core/home`
  \"Remote\" - `remote-manager.core/view`
  \"INI\" - `ini-editor.core/view`"
  [props]
  (let [state @store/state
        {:keys [tab tab-labels]} state]
    (utils/log state)
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
  (r/render-component
   [app {}]
   (.getElementById js/document "app")))
