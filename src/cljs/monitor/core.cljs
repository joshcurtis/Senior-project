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
  {"t" (- (utils/time-seconds) (:initial-time @store/state))
   "Ext-0" (rand-interval 180 220)
   "Ext-1" (rand-interval 100 180)
   "Ext-2" (rand-interval 90 110)
   "Axis-0-x" 0.1
   "Axis-0-y" 0.5
   "Axis-0-z" 0.3
   "Axis-0-a" 0.1
   "Axis-1-x" 0.8
   "Axis-1-y" 0.5
   "Axis-1-z" 0.7
   "Axis-1-a" 0.1
   "Axis-2-x" 0.8
   "Axis-2-y" 0.5
   "Axis-2-z" 0.7
   "Axis-2-a" 0.8
   "Axis-3-x" 0.1
   "Axis-3-y" 0.3
   "Axis-3-z" 0.1
   "Axis-3-a" 0.6})

(utils/set-interval "rand-update"
                    #(controller/update-measurements! (rand-measurements))
                    4000)

(defn contents
  [props]
  (if @(r/cursor store/state [:connection :connected?])
    [view/contents-active]
    [view/contents-inactive]))
