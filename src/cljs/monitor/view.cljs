(ns monitor.view
  (:require
   [monitor.controller :as controller]
   [app.store :as store]
   [utils.core :as utils]
   [utils.widgets :as widgets]
   [reagent.core :as r :refer [atom]]))

(defn contents-inactive
  []
  [:div.jumbotron
   [:h1 "Not Connected"]
   [:p "The application is not connected to a BeagleBone."]
   [:p "Connect in the"
    [:a {:on-click #(utils/click-element "tab-navigation-Remote")
         :style {:cursor "pointer"}} " remote"]
    " tab."]])

(defn temperature-plot
  [{:keys [temperature-group history]}]
  (let [times (get history "t")]
    [widgets/plotly {:style {:width "100%"
                             :height "512px"}
                     :data (map (fn [k] {:mode "lines"
                                         :name k
                                         :x times
                                         :y (get history k)})
                                temperature-group)
                     :layout {:title "Temperatures"
                              :xaxis {:title "Time Elapsed (s)"}
                              :yaxis {:title "Temperature (C°)"}}}]))

(defn axes-plot
  [{:keys [axes-group measurements]}]
  [widgets/plotly {:style {:width "100%"
                           :height "512px"}
                   :data [{:mode "markers"
                           :x (map #(get measurements (:x %1)) axes-group)
                           :y (map #(get measurements (:y %1)) axes-group)
                           :z (map #(get measurements (:z %1)) axes-group)
                           :name (map :name axes-group)
                           :type "scatter3d"}]
                   :layout {:title "Axes"
                            :xaxis {:range [0 1]}
                            :yaxis {:range [0 1]}
                            :zaxis {:range [0 1]}}}])

(defn contents-active
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [is-monitoring? @(r/cursor store/state [:is-monitoring?])
        monitor @(r/cursor store/state [:monitor])
        {:keys [all-components measurements history groups]} monitor]
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
     [temperature-plot {:temperature-group (:temperatures groups)
                        :history history}]
     [axes-plot {:axes-group (:axes groups)
                 :measurements measurements}]
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
