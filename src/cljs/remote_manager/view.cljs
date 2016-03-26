(ns remote-manager.view
  "Provides the view for the remote manager."
  (:require
   [remote-manager.controller :as controller]
   [reagent.core :as r :refer [atom]]))

(defn- disconnected
  [props]
  (let [{:keys [connection]} props
        {:keys [error hostname username password]} connection]
    [:div {}
     (if (some? error)
       [:div.alert.alert-danger {}
        [:h4 {} "Error"]
        [:p error]]
       [:div.alert.alert-warning {}
        [:h4 {} "Disconnected"]
        [:p "There is currently no remote connection!"]])
     [:form.form-horizontal.well {}
      [:fieldset {}
       [:legend {} "Connect Remotely"]
       [:div.form-group {}
        [:label.col-lg-2.control-label {} "hostname"]
        [:input.form-control {:type "text"
                              :on-change #(-> %1
                                              .-target
                                              .-value
                                              controller/set-hostname!)
                              :value hostname}]]
       [:div.form-group {}
        [:label.col-lg-2.control-label {} "user name"]
        [:input.form-control {:type "text"
                              :on-change #(-> %1
                                              .-target
                                              .-value
                                              controller/set-username!)
                              :value username}]]
       [:div.form-group {}
        [:label.col-lg-2.control-label {} "password"]
        [:input.form-control {:type "password"
                              :on-change #(-> %1
                                              .-target
                                              .-value
                                              controller/set-password!)
                              :value password}]]]]
     [:button.btn.btn-primary {:on-click controller/connect!}
      "Connect"]]))

(defn- edit-icon
  [props]
  (let [{:keys [on-click]} props]
    (assert (fn? on-click))
    [:img
     {:src "https://cdn3.iconfinder.com/data/icons/google-material-design-icons/48/ic_mode_edit_48px-128.png"
      :alt "Edit"
      :on-click #(on-click)
      :style {:width "2rem" :height "2rem"}}]))

(defn- render-file
  [props]
  (let [{:keys [directory filename]} props
        full-filename (str "machinekit/configs/" directory filename)]
    (assert (some? directory))
    (assert (some? filename))
    [:tr.active {}
     [:td {} filename]
     [:td {} [edit-icon {:on-click #(controller/edit-file! full-filename)}]]]))

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
    [:div.well {}
     [:h3 {} dir]
     (if loaded?
       [:table.table.table-striped.table-hover {}
        [:thead {}
       [:tr {} [:th {} "File"] [:th {} "Edit"]]]
        [:tbody {}
         (map (fn [f] [render-file {:key f
                                    :directory dir
                                    :filename f}])
              contents)]]
       [:div {} "Loading..."])]))


(defn- connected
  [props]
  (let [{:keys [connection configs]} props
        {:keys [username hostname]} connection]
    (assert (some? connection))
    (assert (some? configs))
    [:div {}
     [:h1 {} (str username \@ hostname)]
     (map (fn [d] [render-config {:key d
                                  :dir d
                                  :contents (get-in configs [:contents d])}])
          (:dirs configs))
     [:button.btn.btn-primary {:style {:margin-right "1rem"}
                               :on-click controller/update-configs!}
      "Refresh"]
     [:button.btn.btn-warning {:on-click controller/disconnect!}
      "Disconnect"]]))

(defn remote-manager
  "Renders a component for editing the machinekit configuration remotely."
  [props]
  (let [{:keys [connection configs]} props
        {:keys [connected?]} connection]
    (assert (some? connection))
    (assert (some? configs))
    (if connected?
      [connected {:connection connection :configs configs}]
      [disconnected {:connection connection}])))
