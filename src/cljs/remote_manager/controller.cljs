(ns remote-manager.controller
  (:require
   [app.store :as store]
   [bbserver.core :as bbserver]
   [utils.core :as utils]
   [ini-editor.controller]
   [text-editor.controller]
   [server-interop.core :as server-interop]
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

(defn clear-configs!
  []
  (swap! store/state assoc
         :configs {:dirs [] :contents {}}))

(defn- --update-configs
  [state configs]
  (let [dirs (mapv identity (keys configs))
        contents configs]
    (assoc state :configs {:dirs dirs
                           :contents contents})))

(defn update-configs!
  "Updates the configs based on the server."
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/configs hostname #(swap! store/state --update-configs %1))))

(utils/set-interval "update-configs"
                    #(let [{:keys [connection]} @store/state]
                       (if (:connected? connection)
                         (update-configs!)))
                    2000)

(defn update-services!
  [log]
  (swap! store/state assoc
         :services (utils/parse-service-log log)))

(defn update-mk-services!
  "Run to parse the ~/Desktop/services.log file
  and update what machinekit services are available"
  []
  (let [hostname (get-in @store/state [:connection :hostname])]
    (bbserver/get-services-log hostname #(update-services! (get %1 "log")))))

(defn try-to-launch-resolver!
  []
  (if (-> @store/state :connection :connected?)
    (let [hostname (get-in @store/state [:connection :hostname])]
      (do
        (utils/log "launching resolver")
        (bbserver/resolve-services hostname #(utils/log %))
        (utils/set-interval "update-services" update-mk-services! 2000)))
    (utils/log "Resolver not started")))

(defn- connect-callback
  [res]
  (let [{:keys [authenticated username]} res]
    (if authenticated
      (do
        (swap! store/state update :connection
               #(merge %1 {:connected? true
                           :connection-pending? false
                           :username username
                           :error nil}))
        (update-configs!)
        (try-to-launch-resolver!))

      (swap! store/state update :connection
             #(merge %1 {:connected? false
                         :connection-pending? false
                         :username nil
                         :error "Incorrect password"})))))

(defn connect!
  []
  (let [{:keys [connection]} @store/state
        {:keys [hostname username password]} connection]
    (swap! store/state update :connection
      #(merge %1 {:connected? false
                  :connection-pending? true
                  :username nil
                  :error nil}))
    (bbserver/login hostname password connect-callback)))

(defn disconnect!
  []
  (swap! store/state update :connection
    #(merge %1 {:connected? false
                :connection-pending? false
                :username nil
                :error nil}
  (utils/clear-interval "update-services"))))

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

(defn test-socket
  []
  (utils/log "Testing socket")
  (server-interop/send-data (utils/encode-buffer MT_PING) #(utils/log %)))

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

(defn log-state
  "Adding whatever information you want to see for debugging here"
  []
  (utils/log (str "Services: " (:services @store/state))))

(defn debug-state
  [timeout]
  (utils/set-interval "debug-state" log-state timeout))

(debug-state 5000)
