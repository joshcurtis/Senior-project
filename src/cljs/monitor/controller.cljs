(ns monitor.controller
  ""
  (:require
   [utils.core :as utils]
   [monitor.model :as model]
   [reagent.core :as r :refer [atom]]))

(defn update-measurements!
  "Updates the measurements if model/is-monitoring? atom has a value of true."
  []
  (if @model/is-monitoring?
      (swap! model/measurements
             model/conj-measurements (model/retrieve-measurements))))

(defn reset-measurements! []
  (reset! model/measurements (model/default-measurements)))

(defn start-monitoring! []
  (reset! model/is-monitoring? true))

(defn stop-monitoring! []
  (reset! model/is-monitoring? false))

(defn toggle-monitoring! []
  (swap! model/is-monitoring? not))
