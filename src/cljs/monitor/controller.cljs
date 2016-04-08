(ns monitor.controller
  ""
  (:require
   [app.store :as store]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(defn enable-monitoring!
  []
  (swap! store/state assoc
         :is-monitoring? true))

(defn disable-monitoring!
  []
  (swap! store/state assoc
         :is-monitoring? false))

(defn toggle-monitoring!
  []
  (swap! store/state
         update :is-monitoring? not))


(defn- --clear-history
  [state]
  (let []
    (-> state
        (assoc :initial-time (utils/time-seconds))
        (update-in [:monitor :history]
                   #(apply hash-map (mapcat (fn [[k v]] [k store/empty-sequence])
                                            %1))))))

(defn clear-history!
  []
  "Clears the history which deletes all data points. The time elapsed is also
  reset."
  (swap! store/state --clear-history))

(defn- --update-measurements
  [state measurements]
  (let [{:keys [monitor is-monitoring?]} state]
    (if is-monitoring?
      (-> state
          (assoc-in [:monitor :measurements] measurements)
          (update-in [:monitor :history] #(merge-with conj %1 %2) measurements))
      state)))

(defn update-measurements!
  "This function only applies if model/is-monitoring? is true. If it is false,
  nothing will happen. Updates the measurements of monitor, as well as adding
  the values to the history."
  [measurements]
  (swap! store/state --update-measurements measurements))
