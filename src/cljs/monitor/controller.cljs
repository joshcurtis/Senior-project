(ns monitor.controller
  ""
  (:require
   [utils.core :as utils]
   [monitor.model :as model]
   [reagent.core :as r :refer [atom]]))

(defn enable-monitoring!
  []
  (reset! model/is-monitoring? true))

(defn disable-monitoring!
  []
  (reset! model/is-monitoring? false))

(defn toggle-monitoring!
  []
  (swap! model/is-monitoring? not))

(defn clear-history!
  []
  "Clears the history which deletes all data points. The time elapsed is also
  reset."
  (reset! model/initial-time (utils/time-seconds))
  (swap! model/monitor model/clear-history))

(defn update-measurements!
  "This function only applies if model/is-monitoring? is true. If it is false,
  nothing will happen. Updates the measurements of monitor, as well as adding
  the values to the history."
  [measurements]
  (if @model/is-monitoring?
    (swap! model/monitor model/update-measurements measurements)))
