(ns monitor.model
  ""
  (:require
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))


(defn default-measurements []
  {:initial-time (utils/time-seconds)
   :times []
   :temperatures {"Extruder-0" {:mode "lines"
                                :name "Extruder-0"
                                :y []}
                  "Extruder-1" {:mode "lines"
                                :name "Extruder-1"
                                :y []}
                  "Extruder-2" {:mode "lines"
                                :name "Extruder-2"
                                :y []}}})


(defonce measurements
  (atom (default-measurements)))

(defn rand-temp
  [bias range]
  (+ bias (rand range)))

(defn retrieve-measurements []
  {:temperatures {"Extruder-0" (rand-temp 170 20)
                  "Extruder-1" (rand-temp 200 20)
                  "Extruder-2" (rand-temp 180 40)}})

(defn conj-to-y
  [m v]
  (update m :y conj v))

(defn conj-temperatures
  [temperatures measured-temps]
  (let []
    (apply hash-map
           (mapcat (fn [[k v]] [k (conj-to-y v (get measured-temps k))])
                   temperatures))))

(defn conj-measurements [measurements measured]
  (let [t (- (utils/time-seconds) (:initial-time measurements))
        times (:times measurements)
        temperatures (:temperatures measurements)]
    (assoc measurements
           :times (conj times t)
           :temperatures (conj-temperatures temperatures (:temperatures measured)))))
