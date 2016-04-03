(ns app.core
  ""
  (:require
   [app.state]
   [ini-editor.core :as ini-editor]
   [remote-manager.core :as remote-manager]
   [server-interop.core :as server-interop]
   [text-editor.core :as text-editor]
   [utils.widgets :as widgets]
   [utils.navbar :as navbar]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))


(defn nil-navbar
  "A navbar with nothing in it. Use it for placeholding"
  [{:keys [title]}]
  [navbar/navbar {:title (or title "")
                  :elements []}])

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
  (let [app-state @app.state/app-state
        tab-labels (:tab-labels app-state)
        tab (:tab app-state)]
    [:div.app {}
     (get topbar-map tab [nil-navbar {}])
     [widgets/tabs {:labels tab-labels
                    :selected tab
                    :on-change #(app.state/set-tab! %1)}]
     [app.state/render-contents]]))

(defn ^:export start
  "Renders the application onto the DOM element \"app\""
  []
  (r/render-component
   [app {}]
   (.getElementById js/document "app")))
