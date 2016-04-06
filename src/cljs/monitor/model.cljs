(ns monitor.model
  ""
  (:require
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(defonce initial-time (atom (utils/time-seconds)))

(defonce is-monitoring? (atom true))

(defonce monitor (atom {:all-components ["Axis-0-x" "Axis-0-y" "Axis-0-z" "Axis-0-a"
                                         "Axis-1-x" "Axis-1-y" "Axis-1-z" "Axis-1-a"
                                         "Axis-2-x" "Axis-2-y" "Axis-2-z" "Axis-2-a"
                                         "Axis-3-x" "Axis-3-y" "Axis-3-z" "Axis-3-a"
                                         "Ext-0" "Ext-1" "Ext-2"]
                        :measurements {"t" nil
                                       "Axis-0-x" nil
                                       "Axis-0-y" nil
                                       "Axis-0-z" nil
                                       "Axis-0-a" nil
                                       "Axis-1-x" nil
                                       "Axis-1-y" nil
                                       "Axis-1-z" nil
                                       "Axis-1-a" nil
                                       "Axis-2-x" nil
                                       "Axis-2-y" nil
                                       "Axis-2-z" nil
                                       "Axis-2-a" nil
                                       "Axis-3-x" nil
                                       "Axis-3-y" nil
                                       "Axis-3-z" nil
                                       "Axis-3-a" nil
                                       "Ext-0" nil
                                       "Ext-1" nil
                                       "Ext-2" nil}
                        :history {"t" '()
                                  "Axis-0-x" '()
                                  "Axis-0-y" '()
                                  "Axis-0-z" '()
                                  "Axis-0-a" '()
                                  "Axis-1-x" '()
                                  "Axis-1-y" '()
                                  "Axis-1-z" '()
                                  "Axis-1-a" '()
                                  "Axis-2-x" '()
                                  "Axis-2-y" '()
                                  "Axis-2-z" '()
                                  "Axis-2-a" '()
                                  "Axis-3-x" '()
                                  "Axis-3-y" '()
                                  "Axis-3-z" '()
                                  "Axis-3-a" '()
                                  "Ext-0" '()
                                  "Ext-1" '()
                                  "Ext-2" '()}
                        :groups {:temperatures ["Ext-0" "Ext-1" "Ext-2"]}}))

(defn update-measurements
  "Updates the `:measurements` `monitor` to be `measurements`. The measurements will
  also be conj'd to `:history`."
  [monitor measurements]
  (assoc monitor
         :measurements measurements
         :history (merge-with conj (:history monitor) measurements)))

(defn clear-history
  "Resets the history to have nothing."
  [monitor]
  (let [ks (-> monitor :history keys)]
    (assoc monitor :history
           (mapcat #(vector %1 '())
                   ks))))
