(ns monitor.core
  "Shows some measurements."
  (:require
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [monitor.controller :as controller]
   [monitor.model :as model]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(defn rand-interval [min max]
  (let [diff (- max min)]
    (+ min (rand diff))))

(defn rand-measurements []
  {"t" (- (utils/time-seconds) @model/initial-time)
   "Ext-0" (rand-interval 280 320)
   "Ext-1" (rand-interval 260 360)
   "Ext-2" (rand-interval 240 250)})

(utils/set-interval "rand-update"
                    #(controller/update-measurements! (rand-measurements))
                    1000)

(defn contents
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [monitor @model/monitor
        {:keys [all-components measurements history groups]} monitor
        temperature-group (:temperatures groups)
        times (get history "t")]
    [:div
     ;; temperature plot
     [widgets/line-plot {:style {:width "100%"
                                 :height "512px"}
                         :data (map (fn [k] {:mode "lines"
                                             :name k
                                             :x times
                                             :y (get history k)})
                                    temperature-group)
                         :layout {:title "Temperatures"
                                  :xaxis {:title "Time Elapsed (s)"}
                                  :yaxis {:title "Temperature (C)"}}}]
     ;; all table
     [:table.table.table-striped.table-hover
      [:thead
       [:tr
        [:th "Name"]
        [:th "Value"]]]
      [:tbody
       (map (fn [k] [:tr {:key k}
                     [:td k]
                     [:td (get measurements k)]])
            all-components)]]]))
