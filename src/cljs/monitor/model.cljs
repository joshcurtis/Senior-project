(ns monitor.model
  ""
  (:require
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(def empty-sequence
  "A sequence is a data structure that can conj a new item. This will be
  converted into a js-array.  Why is this a constant? You can use a list, and
  new items will be put at the front and the plot will be drawn backwards. You
  can use a vector, and items will be pushed back. I don't know which is more
  efficient considering memory, conj'ing, and js conversion."  [])

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
                        :history {"t" empty-sequence
                                  "Axis-0-x" empty-sequence
                                  "Axis-0-y" empty-sequence
                                  "Axis-0-z" empty-sequence
                                  "Axis-0-a" empty-sequence
                                  "Axis-1-x" empty-sequence
                                  "Axis-1-y" empty-sequence
                                  "Axis-1-z" empty-sequence
                                  "Axis-1-a" empty-sequence
                                  "Axis-2-x" empty-sequence
                                  "Axis-2-y" empty-sequence
                                  "Axis-2-z" empty-sequence
                                  "Axis-2-a" empty-sequence
                                  "Axis-3-x" empty-sequence
                                  "Axis-3-y" empty-sequence
                                  "Axis-3-z" empty-sequence
                                  "Axis-3-a" empty-sequence
                                  "Ext-0" empty-sequence
                                  "Ext-1" empty-sequence
                                  "Ext-2" empty-sequence}
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
  (update monitor :history
          #(apply hash-map (mapcat (fn [[k v]] [k empty-sequence])
                                   %1))))
