(ns monitor.core
  "Shows some measurements."
  (:require
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})


(defn contents
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  [:div "Hello"
   [:div {:id "plot-here-derp"
          :style {:height "512px"}} "Loading..."]])


(def trace {:x [1 2 3 4]
            :y [10 15 13 17]
            :type "scatter"})

(def plot-data [trace])

(js/setTimeout #(.newPlot js/Plotly "plot-here-derp" (clj->js plot-data)) 1000)
