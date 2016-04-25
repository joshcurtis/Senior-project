(ns remote-manager.view
  "Provides the view for the remote manager."
  (:require
   [app.store :as store]
   [remote-manager.controller :as controller]
   [utils.navbar :as navbar]
   [reagent.core :as r :refer [atom]]))

(defn- disconnected
  [props]
  (let [{:keys [connection]} props
        {:keys [error hostname username password connection-pending?]} connection]
    [:div {}
      (if (some? error)
        [:div.alert.alert-danger {}
          [:h4 {} "Error"]
          [:p error]]
        [:div.alert.alert-warning {}
          [:h4 {} "Disconnected (No Remote Connection)"]
          [:p "For most default MachineKit images, the default username and password is \"machinekit\"."]])

      [:form.form-horizontal.well {:on-submit (fn [ev] (.preventDefault ev) (controller/connect!))}
        [:label.col-lg-2 {} "Hostname"]
        [:input.form-control {:type "text"
                              :on-change #(-> %1 .-target .-value controller/set-hostname!)
                              :placeholder "Printer Address"
                              :value (or hostname "")}]

        [:label.col-lg-2 {:style {:margin-top "15px"}} "Password"]
        [:input.form-control {:type "password"
                              :on-change #(-> %1 .-target .-value controller/set-password!)
                              :placeholder "Password"
                              :value (or password "")}]

        [:button.btn.btn-primary {:style {:margin-top "15px"}
                                  :type "submit"
                                  :disabled connection-pending?}
          (if connection-pending? "Connecting ..." "Connect")]]
    ]))

(defn- edit-icon
  [props]
  (let [{:keys [on-click]} props]
    (assert (fn? on-click))
    [:img
     {:src "/icons/edit.png"
      :alt "Edit"
      :on-click #(on-click)
      :style {:cursor "pointer"
              :width "2rem"
              :height "2rem"}}]))

(defn- save-icon
  [props]
  (let [{:keys [on-click]} props]
    (assert (fn? on-click))
    [:img
     {:src "/icons/save.png"
      :alt "Save"
      :on-click #(on-click)
      :style {:cursor "pointer"
              :width "2rem"
              :height "2rem"}}]))

(defn- delete-icon
  [props]
  (let [{:keys [on-click]} props]
    (assert (fn? on-click))
    [:img
     {:src "/icons/delete.png"
      :alt "Delete"
      :on-click #(on-click)
      :style {:cursor "pointer"
              :width "2rem"
              :height "2rem"}}]))

(defn- render-file
  [props]
  (let [{:keys [directory filename]} props
        config directory
        full-filename (str "machinekit/configs/" directory filename)]
    (assert (some? directory))
    (assert (some? filename))
    [:tr {}
     [:td {:style {:width "40%"}} filename]
     [:td {:style {:width "20%"}} [edit-icon {:on-click #(controller/edit-file! config filename)}]]
     [:td {:style {:width "20%"}} [save-icon {:on-click #(controller/download-file! config filename)}]]
     [:td {:style {:width "20%"}} [delete-icon {:on-click #(controller/delete-file! config filename)}]]]))

(defn- render-config
  "
  # Props
  dir - directory of config
  contents - vector of files within dir or nil if they haven't been loaded yet."
  [props]
  (let [{:keys [dir contents]} props
        loaded? (some? contents)
        contents (or contents [])]
    (assert (some? dir))
    [:div {}
      (if (= dir "")
        [:h3 {:style {:paddingLeft "8px" :margin "00px 0px 10px 0px"}} "."]
        [:h3 {:style {:paddingLeft "8px" :margin "40px 0px 10px 0px"}} dir])

      (if loaded?
        [:table.table.table-striped.table-hover {}
         [:thead {}
          [:tr {}
           [:th {:style {:width "40%"}} "File"]
           [:th {:style {:width "20%"}} "Edit"]
           [:th {:style {:width "20%"}} "Download"]
           [:th {:style {:width "20%"}} "Delete"]]]
         [:tbody {}
          (map (fn [f] [render-file {:key f
                                     :directory dir
                                     :filename f}]) contents)]]
        [:div {} (str "Loading " dir " ...")])
    ]))

(defn- control-buttons
  [services-running?]
  (let [button-style {:margin-right "1rem"}]
    [:div {:style {:marginBottom "10px"}}
      [:button.btn.btn-primary {:style button-style
                                :on-click controller/update-configs!} "Refresh"]
      [:button.btn.btn-warning {:style button-style
                                :on-click controller/disconnect!} "Disconnect"]

      (if services-running?
        [:button.btn.btn-danger {:style button-style
                                 :on-click controller/shutdown-mk!} "Shutdown MachineKit"]
        [:button.btn.secondary {:style button-style
                                :on-click controller/run-mk!} "Launch MachineKit"])
    ]))

(defn- connected
  ;; TODO fix repetition of ':margin-right "1rem"'
  [props]
  (let [{:keys [connection configs services-running?]} props
        {:keys [username hostname]} connection]
    (assert (some? connection))
    (assert (some? configs))
    [:div {}
     [:h1 {} (str username \@ hostname)]
     [:div.well
      (str "Remote configurations should be located in ~/machinekit/configs/. Running machinekit for the first time will load the configurations onto this directory.")]
     (control-buttons services-running?)
     [:div.panel.panel-default
      [:div.panel-body
       (map (fn [d] [render-config {:key d
                                    :dir d
                                    :contents (get-in configs [:contents d])}])
            (:dirs configs))]]
    ]))

(defn remote-manager
  "Renders a component for editing the machinekit configuration remotely."
  [props]
  (let [{:keys [connection configs services]} props
        {:keys [connected?]} connection]
    (assert (some? connection))
    (assert (some? configs))
    (if connected?
      [connected {:connection connection :configs configs
                  :services-running? (not (empty? services))}]
      [disconnected {:connection connection}])))
