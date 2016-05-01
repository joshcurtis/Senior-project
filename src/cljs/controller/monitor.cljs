(ns controller.monitor
  "Actions for fetching and updating remote data."
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
   [model.core :as model]
   [utils.core :as utils]
   [cljs.reader :as reader]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(defn enable-monitoring!
  []
  (swap! model/state assoc
         :is-monitoring? true))

(defn disable-monitoring!
  []
  (swap! model/state assoc
         :is-monitoring? false))

(defn toggle-monitoring!
  []
  (swap! model/state
         update :is-monitoring? not))


(defn- --clear-history
  [state]
  (let []
    (-> state
        (assoc :initial-time (utils/time-seconds))
        (update-in [:monitor :history]
                   #(apply hash-map
                           (mapcat (fn [[k v]] [k []])
                                   %1))))))

(defn clear-history!
  []
  "Clears the history which deletes all data points. The time elapsed is also
  reset."
  (swap! model/state --clear-history))

(defn- conj-and-cut-fn
  [max-len cut-to-len]
  (fn [coll el]
    (assert (vector? coll))
    (let [coll (conj coll el)
          len (count coll)]
      (if (> len max-len)
        (subvec coll (- len cut-to-len))
        coll))))

(defn- --update-measurements
  [state measurements]
  (let [{:keys [monitor is-monitoring?]} state
        {:keys [history-display-size history-store-size]} monitor]
    (if is-monitoring?
      (-> state
          (assoc-in [:monitor :measurements] measurements)
          (update-in [:monitor :history]
                     #(merge-with (conj-and-cut-fn history-store-size
                                                   history-display-size)
                                  %1
                                  %2)
                     measurements))
      state)))

(defn update-measurements!
  "This function only applies if model/is-monitoring? is true. If it is false,
  nothing will happen. Updates the measurements of monitor, as well as adding
  the values to the history."
  []
  (let [hostname (model/hostname)]
    (if (and (some? hostname)
             (:is-monitoring? @model/state))
      (go (let [url (str "http://" hostname ":3001/measure")
                res (<! (http/get url
                                  {:with-credentials? false}))
                t (- (utils/time-seconds) (:initial-time @model/state))
                measurements (-> res :body reader/read-string (assoc "t" t))]
            (swap! model/state --update-measurements measurements))))))

(defn ith-csv-row
  [history keys i]
  (string/join ", "
               (map #(get-in history [% i]) keys)))

(defn history-to-csv
  [history]
  (let [fields (conj
                (sort (remove #(= "t" %1) (keys history)))
                "t")
        len (count (get history "t"))]
    (str (string/join ", " fields) \newline
         (string/join \newline (map #(ith-csv-row history fields %)
                                    (range len))))))

(defn download-measurements!
  []
  (utils/save-file
   (history-to-csv (-> @model/state
                       :monitor
                       :history))
   "measurements.csv"))
