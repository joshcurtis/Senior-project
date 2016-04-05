(ns monitor.core
  "Shows some measurements."
  (:require
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [monitor.controller :as controller]
   [monitor.model :as model]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(def update-interval 3000)
(utils/set-interval "monitor.controller/update-measurements!"
                    controller/update-measurements!
                    update-interval)

(defn plot-temperatures [temperatures times]
  (let [data (map (fn [[k v]] (assoc v :x times))
                  temperatures)]
    [widgets/line-plot {:data data
                        :layout {:title "Temperatures"}}]))

(defn contents
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [measurements @model/measurements
        {:keys [times temperatures]} measurements]
    [:div
     [:div
      [plot-temperatures temperatures times]]
     [:div
      [:button.btn {:class (if @model/is-monitoring? "btn-warning" "btn-primary")
                    :on-click controller/toggle-monitoring!}
       (if @model/is-monitoring? "Stop Monitoring" "Start Monitoring")]
      [:button.btn.btn-default {:on-click controller/reset-measurements!} "Reset"]]]))
