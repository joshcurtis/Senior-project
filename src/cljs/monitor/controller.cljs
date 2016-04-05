(ns monitor.controller
  ""
  (:require
   [utils.core :as utils]
   [monitor.model :as model]
   [reagent.core :as r :refer [atom]]))

(defn update-measurements! []
  (swap! model/measurements model/conj-measurements (model/retrieve-measurements)))

(defn reset-measurements! []
  (reset! model/measurements (model/default-measurements)))
