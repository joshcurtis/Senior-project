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

(defn set-hostname!
  [name]
  (swap! store/state assoc-in [:connection :hostname] name))

(defn set-password!
  [password]
  (swap! store/state assoc-in [:connection :password] password))

(defn- update-configs-callback
  "Callback for a file list in the machinekit config directory."
  [status error-code body]
  (if (and (= status 200) (some? body))
    (let [dirs (mapv identity (keys body))]
      (swap! store/state assoc :configs {:dirs dirs :contents body}))))

(defn update-configs!
  "Requests a file list in the machinekit config directory."
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/configs hostname update-configs-callback)))

; Update the configs while the user is connected
(utils/set-interval "update-configs"
  #(let [{:keys [connection]} @store/state]
    (if (:connected? connection) (update-configs!)))
  2000)

(defn- update-mk-services-callback
  "Callback for MachinkeKit services list."
  [status error-code body]
  (if (and (= status 200) (some? body))
    (swap! store/state assoc :services (utils/parse-service-log body))))

(defn update-mk-services!
  "Request the ~/Desktop/services.log file which will
  be parsed to update and inform the user what machinekit
  services are available"
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/get-services-log hostname update-mk-services-callback)))

(defn try-to-launch-resolver!
  []
  (if (-> @store/state :connection :connected?)
    (let [hostname (get-in @store/state [:connection :hostname])]
      (do
        (utils/log "launching resolver")
        (bbserver/resolve-services hostname #(utils/log %))
        (utils/set-interval "update-services" update-mk-services! 2000)))
    (utils/log "Resolver not started")))

(defn- merge-with-state-connection
  [merge-map]
  (swap! store/state update :connection #(merge %1 merge-map)))

(defn- connect-callback
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
          (try-to-launch-resolver!))))))

(defn connect!
  []
  (let [{:keys [connection]} @store/state
        {:keys [hostname username password]} connection]
    (merge-with-state-connection {:connected? false
                                  :connection-pending? true
                                  :username nil
                                  :error nil})
    (bbserver/login hostname password connect-callback)))

(defn disconnect!
  []
  (merge-with-state-connection {:connected? false
                                :connection-pending? false
                                :username nil
                                :error nil})
  (utils/clear-interval "update-services"))

(defn run-mk!
  []
  (if (-> @store/state :connection :connected?)
    (let [hostname (get-in @store/state [:connection :hostname])]
      (bbserver/run_mk hostname #(utils/log %)))))

(defn shutdown-mk!
  []
  (if (-> @store/state :connection :connected?)
    (let [hostname (get-in @store/state [:connection :hostname])]
      (bbserver/stop_mk hostname #(utils/log %)))))

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
        callback #(callback (get %1 "contents") [:remote filename])]
    (bbserver/get-file hostname config filename callback)))

(defn upload-file!
  [config filename contents]
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/put-file hostname config filename contents update-configs!)))

(defn download-file!
  [config filename]
  (let [hostname (get-in @store/state [:connection :hostname])
        callback #(utils/save-file (get %1 "contents") filename)]
    (bbserver/get-file hostname config filename callback)))

(defn delete-file!
  [config filename]
  (let [hostname (get-in @store/state [:connection :hostname])
        callback update-configs!]
    (bbserver/delete-file hostname config filename callback)))

(defn ping
  []
  (let [hostname (get-in @store/state [:connection :hostname])
        port (get-in @store/state [:services :config])]
    (bbserver/ping hostname port #(utils/log "Pinging MachineKit"))))

(defn log-state
  "Adding whatever information you want to see for debugging here"
  []
  (utils/log (str "Services: " (:services @store/state))))

(defn debug-state
  [timeout]
  (utils/set-interval "debug-state" log-state timeout))

(debug-state 5000)
