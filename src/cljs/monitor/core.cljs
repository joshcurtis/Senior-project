(ns monitor.core
  "Shows some measurements."
  (:require
   [utils.widgets :as widgets]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(def legend-template
  ""
  "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"></span><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>")


(defn contents
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  [:div
   [widgets/line-plot {:width 512
                       :height 256
                       :options {:animation false
                                 :bezierCurve true
                                 :legendTemplate legend-template}
                       :data {:labels (range 15)
                              :datasets [{:label "EXT01 Temperature"
                                          :fillColor "rgba(0, 0, 0, 0)"
                                          :data (range 15)}]}}]
   ])
