(ns monitor.view
  (:require
   [monitor.controller :as controller]
   [app.store :as store]
   [utils.core :as utils]
   [utils.widgets :as widgets]
   [utils.rd3 :as rd3]
   [three.core :as three]
   [reagent.core :as r :refer [atom]]))

(defn contents-inactive
  []
  [:div.jumbotron
   [:h1 "Not Connected"]
   [:p "The application is not connected to a BeagleBone."]
   [:p "Connect in the "
    [:a {:on-click #(utils/click-element "tab-navigation-Remote")
         :style {:cursor "pointer"}} "Remote"]
    " tab."]])

(defn n-most-recent
  [coll n]
  {:pre [(vector? coll)]}
  (let [len (count coll)
        start (max 0 (- len n))]
    (subvec coll start)))

(defn format-rd3-line-values [x y]
  {:pre [(= (count x) (count y))]}
  (let [len (count x)]
    (map (fn [i] {:x (get x i) :y (get y i)})
         (range len))))

(defn temperature-plot
  [props]
  (let [{:keys [temperature-group history display-len]} props
        times (n-most-recent (get history "t") display-len)
        data (map (fn [k] {:name k
                           :values (format-rd3-line-values
                                    times
                                    (n-most-recent (get history k)
                                                   display-len))})
                  temperature-group)]
    (if (pos? (count times))
      [rd3/line-chart {:data data
                       :legend true
                       :width (- js/document.body.clientWidth 32)
                       :height 320
                       :title "Temperature"
                       :gridHorizontal true
                       :hoverAnimation true
                       :circle-radius 5
                       :margins {:top 10 :right 20 :bottom 80 :left 50}
                       :y-axis-label "Temperature (C°)"
                       :x-axis-label "Time Elapsed (s)"}]
      [:div.jumbotron
       [:h1 "No Data Collected"]
       [:p "Resume monitoring to collect more data."]])))

(defn format-coords
  "Format coordinates for use with `three/axes-plot`. Major hacks include
  switching `y` and `z` and coordinates the original `y` is negated."
  [measurements m]
  (let [x (get measurements (:x m))
        y (get measurements (:y m))
        z (get measurements (:z m))
        [x y z] [x z (- y)]]
    {:x x
     :y y
     :z z}))

(defonce render-tick (atom 0))

(utils/set-interval "render-tick"
                    #(swap! render-tick inc)
                    (/ 1000 30))

(defn axes-plot
  [{:keys [axes-group measurements]}]
  (let [axes (mapv #(format-coords measurements %)
                   axes-group)]
    [:div.r-axes-plot
     [:h2 "Axes"]
     [three/axes-plot
      {:max-axes 6
       :background "#EEEEEE"
       :size {:width 512
              :height 512}
       :render-tick @render-tick
       :controls true
       :camera {:x 0.0
                :y 1.0
                :z 1.5
                :atx 0.0
                :aty 10.0
                :atz 0.0
                :upx 0
                :upy 1
                :upz 0.5}
       :update-camera @(r/cursor store/state [:monitor :reset-camera])
       :light {:x 0.0
               :y 1.6
               :z 0.0}
       :axes axes}]
     [:button.btn.btn-primary
      {:on-click
       (fn []
         (swap! store/state assoc-in [:monitor :reset-camera] true)
         (js/setTimeout #(swap! store/state assoc-in [:monitor :reset-camera] false) 30))}
      "Reset Camera"]]))

(defn table-measurements
  [props]
  (let [{:keys [all-components measurements]} props]
    [:table.table.table-striped.table-hover
     [:thead
      [:tr
       [:th "Name"]
       [:th "Value"]]]
     [:tbody
      (map (fn [k] [:tr {:key k}
                    [:td k]
                    [:td (get measurements k)]])
           all-components)]]))

(defn contents-active
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [is-monitoring? @(r/cursor store/state [:is-monitoring?])
        monitor @(r/cursor store/state [:monitor])
        monitor-tab @(r/cursor store/state [:monitor-tab])
        {:keys [all-components
                measurements
                history
                history-display-size
                groups]} monitor]
    [:div
     [:div {:style {:margin-bottom "2rem"}}
      [:button.btn.btn-sm
       {:class (if is-monitoring? "btn-primary" "btn-secondary")
        :on-click controller/toggle-monitoring!
        :style {:margin-right "1rem"}}
       (if is-monitoring? "Pause Monitoring" "Resume Monitoring")]
      [:button.btn.btn-warning.btn-sm
       {:on-click controller/clear-history!
        :style {:margin-right "1rem"}}
       "Clear History"]
      [:button.btn.btn-info.btn-sm
       {:style {:margin-right "1rem"}
        :on-click controller/download-measurements!}
       "Download CSV"]]
     ;; temperature plot
     [widgets/tabs {:labels ["Measurements" "Axes" "History"]
                    :id-prefix "monitor-tab"
                    :selected monitor-tab
                    :on-change #(swap! store/state assoc :monitor-tab %)}]
     (case monitor-tab
       "History"
       [temperature-plot {:temperature-group (:temperatures groups)
                          :history history
                          :display-len history-display-size}]
       "Axes"
       [axes-plot {:axes-group (:axes groups)
                   :measurements measurements}]
       "Measurements"
       [table-measurements {:all-components all-components
                            :measurements measurements}])]))
