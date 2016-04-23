(ns monitor.core
  "Shows some measurements."
  (:require
   [monitor.view :as view]
   [app.store :as store]
   [monitor.controller :as controller]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(defn rand-interval [min max]
  (let [diff (- max min)]
    (+ min (rand diff))))

(defn rand-measurements []
  (let [t (- (utils/time-seconds) (:initial-time @store/state))
        s (mod t 20)]
    {"t" t
     "Ext-0" (rand-interval 180 220)
     "Ext-1" (rand-interval 100 180)
     "Ext-2" (rand-interval 90 110)
     "Axis-0-x" (/ s 15.0)
     "Axis-0-y" 0.0
     "Axis-0-z" 0.0
     "Axis-0-a" 0.0
     "Axis-1-x" (/ s -15.0)
     "Axis-1-y" 0.0
     "Axis-1-z" 0.0
     "Axis-1-a" 0.0
     "Axis-2-x" 0.0
     "Axis-2-y" (/ s 15.0)
     "Axis-2-z" 0.0
     "Axis-2-a" 0.0
     "Axis-3-x" (rand-interval 0 0.05)
     "Axis-3-y" (rand-interval 0 0.05)
     "Axis-3-z" (rand-interval 0 0.05)
     "Axis-3-a" 0.0}))

(utils/set-interval "rand-update"
                    #(controller/update-measurements! (rand-measurements))
                    1000)

(defn contents
  [props]
  (if @(r/cursor store/state [:connection :connected?])
    [view/contents-active]
    [view/contents-inactive]))
