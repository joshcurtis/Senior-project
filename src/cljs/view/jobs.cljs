(ns view.jobs
  "Provides a view into the jobs that are running on the printer."
  (:require
   [model.core :as model]
   [utils.core :as utils]
   [reagent.core :as r]))

(def mock-state
  {:jobs {:active-job {:name "car.gcode"
                       :started (js/Date.now)}
          :queued-jobs [{:name "glasses.gcode"}
                        {:name "case.gcode"}]
          :finished-jobs [{:name "box.gcode"
                           :finished (- (js/Date.now) 60000)}
                          {:name "letter.gcode"
                           :finished (- (js/Date.now) 120000)}]}})
(def state (r/atom mock-state))

(defn alert-unimplemented
  []
  (js/alert "Unimplemented"))

(defn upload-job
  []
  [:div.well
   [:p "Welcome to MachineView's job manager. Upload a g-code job for the CNC
   machine. Don't have a g-code file? Many applications such as "
    [:a {:href "slic3r.org"
         :target "_blank"}
     "Slic3r"]
    " exist to convert common 3D formats into g-code."]
   [:button.btn.btn-primary
    "Upload Job"]])

(defn active-job
  [job]
  (if (some? job)
    [:div.well
     [:h1 "Current Job"]
     [:h2 [:code (:name job)]]
     [:div "Started " (utils/timestamp-to-str (:started job))]
     [:div.progress.progress-striped.active {:style {:margin-top "4rem"}}
      [:div.progress-bar {:style {:width "100%"}}]]
     [:button.btn.btn-danger {:on-click alert-unimplemented}
      "Cancel Job"]]
    [:div.well
     [:h1 "No Active Job"]
     [:p "Upload a job and monitor it!"]]))

(defn queued-jobs
  [q]
  [:div.well
   [:h1 "Queued Jobs"]
   (map (fn [{:keys [name]}]
          [:div {:key name}
           [:code {:style {:min-width "32rem"
                           :display "inline-block"}} name]
           [:button.btn.btn-danger.btn-sm {:style {:width "12rem"}
                                    :on-click alert-unimplemented}
            "Remove Job"]])
        q)])

(defn finished-jobs
  [jobs]
  [:div.well
   [:h1 "Completed Jobs"]
   (map (fn [{:keys [name finished]}]
          [:div {:key finished}
           [:div [:code name]
            " finished on " (utils/timestamp-to-str finished)]])
        jobs)])

(defn contents
  []
  (let [jobs (:jobs @state)]
    [:div
     [:h1 "Jobs"]
     [upload-job]
     [active-job (:active-job jobs)]
     [queued-jobs (:queued-jobs jobs)]
     [finished-jobs (:finished-jobs jobs)]]))
