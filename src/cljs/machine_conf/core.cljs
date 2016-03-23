(ns machine-conf.core
  ""
  (:require
   [ini-editor.core]
   [machine-conf.remote :as remote]
   [machine-conf.widgets :as widgets]
   [machine-conf.utils :as utils]
   [reagent.core :as r :refer [atom]]))


(defonce app-state (atom {:tab "Home"}))
(defn set-tab! [label] (swap! app-state assoc :tab label))

(remote/server-println "Connected!")

(defn home
  "reagent-component for home tab."
  [props]
  [:div {} "Home Tab"])

(defn app
  ""
  [props]
  (let [tab (:tab @app-state)]
    [:div.app {}
     [widgets/tabs {:labels ["Home" "INI"]
                    :selected tab
                    :on-change #(set-tab! %1)}]
     [:div.tab-content {:style {:margin "1rem"}}
      [:div.tab-pane.active {}
      (cond
        (= tab "Home") [home {}]
        (= tab "INI") [ini-editor.core/view]
        :else [:div {} "Unknown tab"])]]]))

(defn ^:export start
  "Renders the application onto the DOM element 'app'"
  []
  (r/render-component
   [app {}]
   (.getElementById js/document "app")))
