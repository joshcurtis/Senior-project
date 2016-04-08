(ns monitor.core
  "Shows some measurements."
  (:require
   [app.store :as store]
   [monitor.controller :as controller]
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [remote-manager.model]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(defn rand-interval [min max]
  (let [diff (- max min)]
    (+ min (rand diff))))

(defn rand-measurements []
  {"t" (- (utils/time-seconds) (:initial-time @store/state))
   "Ext-0" (rand-interval 180 220)
   "Ext-1" (rand-interval 100 180)
   "Ext-2" (rand-interval 90 110)})

(utils/set-interval "rand-update"
                    #(controller/update-measurements! (rand-measurements))
                    1000)

(defn contents-inactive
  []
  [:div.jumbotron
   [:h1 "Not Connected"]
   [:p "The application is not connected to a BeagleBone."]
   [:p "Connect in the"
    [:a {:on-click #(utils/click-element "tab-navigation-Remote")
         :style {:cursor "pointer"}} " remote"]
    " tab."]])

(defn contents-active
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [{:keys [is-monitoring? monitor]} @store/state
        {:keys [all-components measurements history groups]} monitor
        temperature-group (:temperatures groups)
        times (get history "t")]
    [:div
     ;; temperature plot
     [:div
      [:button.btn {:class (if is-monitoring? "btn-primary" "btn-secondary")
                    :on-click controller/toggle-monitoring!
                    :style {:margin-right "1rem"}}
       (if is-monitoring? "Pause Monitoring" "Resume Monitoring")]
      [:button.btn.btn-warning {:on-click controller/clear-history!
                                :style {:margin-right "1rem"}}
       "Clear History"]]
     [widgets/line-plot {:style {:width "100%"
                                 :height "512px"}
                         :data (map (fn [k] {:mode "lines"
                                             :name k
                                             :x times
                                             :y (get history k)})
                                    temperature-group)
                         :layout {:title "Temperatures"
                                  :xaxis {:title "Time Elapsed (s)"}
                                  :yaxis {:title "Temperature (C°)"}}}]
     ;; all table
     [:table.table.table-striped.table-hover
      [:thead
       [:tr
        [:th "Name"]
        [:th "Value"]]]
      [:tbody
       (map (fn [k] [:tr {:key k}
                     [:td k]
                     [:td (get measurements k)]])
            all-components)]]]))

(defn contents
  [props]
  (if (:connected? @remote-manager.model/connection)
    [contents-active]
    [contents-inactive]))
