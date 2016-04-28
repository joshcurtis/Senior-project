(ns remote-manager.controller
  (:require
   [app.store :as store]
   [bbserver.core :as bbserver]
   [utils.core :as utils]
   [ini-editor.controller]
   [text-editor.controller]
   [clojure.string :as string]))

(defonce mt (.-protobuf js/machinetalk))
(defonce container-types (.-ContainerType (.-message mt)))
(defonce MT_PING (.-MT_PING container-types))
(defonce MT_SHUTDOWN (.-MT_SHUTDOWN container-types))

; Forward declare
(declare
  start-update-configs-and-services-interval
  start-update-running-interval)

(defn set-hostname!
  [name]
  (swap! store/state assoc-in [:connection :hostname] name))

(defn set-password!
  [password]
  (swap! store/state assoc-in [:connection :password] password))

(defn- merge-with-state-connection
  [merge-map]
  (swap! store/state update :connection #(merge %1 merge-map)))

(defn- update-configs-callback
  "Callback for a file list in the machinekit config directory."
  [status error-code body]
  (if (and (= status 200) (some? body))
    (let [dirs (mapv identity (keys body))]
      (swap! store/state assoc :configs {:dirs dirs :contents body}))))

(defn- update-configs!
  "Requests a file list in the machinekit config directory."
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/configs hostname update-configs-callback)))

(defn- update-mk-services-callback
  "Callback for MachinkeKit services list."
  [status error-code body]
  (if (and (= status 200) (some? body))
    (swap! store/state assoc :services (utils/parse-service-log (get body "log")))))

(defn- update-mk-services!
  "Request the ~/Desktop/services.log file which will
  be parsed to update and inform the user what machinekit
  services are available"
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/get-services-log hostname update-mk-services-callback)))

(defn- log-body
  [status error-code body]
  (if (and (= status 200) (some? body))
    (utils/log body)
    (utils/log [:status status :error-code error-code])))

(defn- try-to-launch-resolver!
  "Request that services be resolved."
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (do
      (utils/log "Launching Resolver")
      (bbserver/resolve-services hostname log-body))))

(defn- update-running-callback
  [status error-code body]
  (cond
    (or (= error-code :http-error) (= error-code :timeout))
      (merge-with-state-connection {:connected? false
                                    :connection-pending? false
                                    :username nil
                                    :error "Disconnected from BeagleBone"})
    :else
      (swap! store/state assoc :running? body)
  ))

(defn- update-running!
  []
  "Request to see if MachinkeKit is running."
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/running hostname update-running-callback)))

(defn- connect-callback
  "Callback for connection/login attempt with the BeagleBone."
  [status error-code body]
  (cond
    (or (= error-code :http-error) (= error-code :timeout))
      (merge-with-state-connection {:connected? false
                                    :connection-pending? false
                                    :username nil
                                    :error "Unable to connect with host"})
    (nil? body)
      (merge-with-state-connection {:connected? false
                                    :connection-pending? false
                                    :username nil
                                    :error "Error with response body"})
    :else
    (let [{:keys [authenticated username]} body]
      (if (not authenticated)
        (merge-with-state-connection {:connected? false
                                      :connection-pending? false
                                      :username nil
                                      :error "Incorrect password"})
        (do
          (merge-with-state-connection {:connected? true
                                        :connection-pending? false
                                        :username username
                                        :error nil})
          (update-configs!)
          (try-to-launch-resolver!)
          (start-update-configs-and-services-interval)
          (start-update-running-interval))))
  ))

(defn connect!
  "Request authorization to access the BeagleBone."
  []
  (let [{:keys [connection]} @store/state
        {:keys [hostname username password]} connection]
    (merge-with-state-connection {:connected? false
                                  :connection-pending? true
                                  :username nil
                                  :error nil})
    (bbserver/login hostname password connect-callback)))

(defn disconnect!
  "Disconnect from the BeagleBone and return to the login screen."
  []
  (merge-with-state-connection {:connected? false
                                :connection-pending? false
                                :username nil
                                :error nil})
  (utils/clear-interval "update-configs-and-services")
  (utils/clear-interval "update-running"))

(defn run-mk!
  []
  (if (-> @store/state :connection :connected?)
    (let [hostname (get-in @store/state [:connection :hostname])]
      (do
        (bbserver/run_mk hostname log-body)
        (swap! store/state assoc :running? true)))))

(defn shutdown-mk!
  []
  (if (-> @store/state :connection :connected?)
    (let [hostname (get-in @store/state [:connection :hostname])]
      (do
        (bbserver/stop_mk hostname log-body)
        (swap! store/state assoc :running? false)))))

(defn- edit-ini!
  [s id]
  (assert (string? s))
  (assert (some? id))
  (ini-editor.controller/load-str! id s)
  (utils/click-element "tab-navigation-INI"))

(defn- edit-unsupported
  [s id]
  (text-editor.controller/load-text! id s)
  (utils/click-element "tab-navigation-Text"))

(def edit-callbacks {"ini" edit-ini!})

(defn edit-file!
  [config filename]
  (let [extension (utils/file-ext filename)
        hostname (get-in @store/state [:connection :hostname])
        callback (get edit-callbacks extension edit-unsupported)
        callback #(callback (get %3 "contents") [:remote filename])]
    (bbserver/get-file hostname config filename callback)))

(defn upload-file!
  [config filename contents]
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/put-file hostname config filename contents update-configs!)))

(defn download-file!
  [config filename]
  (let [hostname (get-in @store/state [:connection :hostname])
        callback #(utils/save-file (get %3 "contents") filename)]
    (bbserver/get-file hostname config filename callback)))

(defn delete-file!
  [config filename]
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/delete-file hostname config filename update-configs!)))

(defn ping
  []
  (let [hostname (get-in @store/state [:connection :hostname])
        port (get-in @store/state [:services :config])]
    (bbserver/ping hostname port #(utils/log "Pinging MachineKit"))))

; Create an update interval while the user is connected that
; updates configs and mk-services
(defn- start-update-configs-and-services-interval
  []
  (utils/set-interval "update-configs-and-services"
    #(let [{:keys [connection]} @store/state]
      (if (:connected? connection)
        (do
          (update-configs!)
          (update-mk-services!))))
    500))

; Create an update interval while the user is connected that
; updates whether MachineKit is running
(defn- start-update-running-interval
  []
  (utils/set-interval "update-running"
    #(let [{:keys [connection]} @store/state]
      (if (:connected? connection)
        (update-running!)))
    500))

; Create a debug logging interval
(utils/set-interval "debug-state"
  #(do
    (utils/log (str "Services: " (:services @store/state)))
   )
  5000)
