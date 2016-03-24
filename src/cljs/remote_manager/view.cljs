(ns remote-manager.view
  "Provides the view for the remote manager."
  (:require
   [remote-manager.controller :as controller]
   [reagent.core :as r :refer [atom]]))

(defn disconnected
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

(defn connected
  [props]
  (let [{:keys [connection]} props
        {:keys [username hostname]} connection]
    [:div {}
     [:h1 {} (str username \@ hostname)]
     [:button.btn.btn-warning {:on-click controller/disconnect!}
      "Disconnect"]]))

(defn remote-manager
  "Renders a component for editing the machinekit configuration remotely."
  [props]
  (let [{:keys [connection]} props
        {:keys [connected?]} connection]
    (if connected?
      [connected {:connection connection}]
      [disconnected {:connection connection}])))
