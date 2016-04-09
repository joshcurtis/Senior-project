(ns remote-manager.controller
  (:require
   [app.store :as store]
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

(defn set-username!
  [name]
  (swap! store/state assoc-in [:connection :username] name))

(defn set-password!
  [password]
  (swap! store/state assoc-in [:connection :password] password))

(defn clear-configs!
  []
  (swap! store/state assoc
         :configs {:dirs [] :contents {}}))

(defn update-config!
  "Updates a single config based on the server. There isn't really a reason to
  call this function. Prefer to use `update-configs!`, which uses this function."
  [dir]
  (assert (utils/dir? dir))
  (let [callback (fn [files]
                   (swap! store/state assoc-in [:configs :contents dir] files))]
    (server-interop/sftp-ls (str "machinekit/configs/" dir) callback)))

(defn update-configs!
  "Updates the configs based on the server."
  []
  (let [valid-dir #(or (utils/dir? %) (= "" %))
        callback (fn [dirs]
                   (swap! store/state assoc-in
                          [:configs :dirs] (filterv valid-dir (into [""] dirs)))
                   (swap! store/state assoc-in
                          [:configs :contents ""] (filterv (complement utils/dir?) dirs))
                   (doseq [dir dirs] (if (valid-dir dir) (update-config! dir))))]
    (clear-configs!)
    (server-interop/sftp-ls "machinekit/configs/" callback)))

(utils/set-interval "update-configs-when-empty"
                    #(let [{:keys [connection configs]} @store/state]
                       (if (and (:connected? connection)
                                (empty? (:dirs configs)))
                         (update-configs!)))
                    2000)

(defn update-services!
  [log]
  (swap! store/state assoc
         :services (utils/parse-resolve-log log)))

(defn update-mk-services!
  "Run to parse the ~/Desktop/services.log file
  and update what machinekit services are available"
  []
  (server-interop/sftp-get "~/Desktop/services.log" update-services!))

(defn try-to-launch-resolver!
  []
  (if (-> @store/state :connection :connected?)
    (do
      (utils/log "launching resolver")
      (server-interop/watch-mk-services! utils/log-ssh-cmd)
      (utils/set-interval "update-services" update-mk-services! 2000))
    (utils/log "Resolver not started")))

(defn connect!
  []
  (let [{:keys [connection]} @store/state
        {:keys [hostname username password]} connection
        callback (fn [res]
                   (if (nil? res) (do
                                    (swap! store/state update :connection
                                           #(merge %1 {:connected? true
                                                       :connection-pending? false
                                                       :error nil}))
                                    (update-configs!)
                                    (try-to-launch-resolver!))
                       (swap! store/state update :connection
                              #(merge %1 {:connected? false
                                          :connection-pending? false
                                          :error res}))))]
    (swap! store/state update :connection
           #(merge %1 {:connection-pending? true
                       :error nil}))
    (server-interop/ssh-connect! hostname username password callback)))

(defn disconnect!
  []
  (utils/clear-interval "update-services")
  (utils/clear-interval "debug-state")
  (server-interop/cleanup! server-interop/ssh-disconnect!)
  (swap! store/state update :connection
         #(merge %1 {:connected? false
                     :connection-pending? false
                     :error nil})))

(defn- print-available-services
  []
  (println (:services @store/state)))

(defn- log-available-services
  []
  (utils/log "Services: ")
  (-> @store/state :services str utils/log))

(defn launch-mk!
  []
  (if (-> @store/state :connection :connected?)
    (do
      (.log js/console "Trying to launch mk")
      (server-interop/launch-mk! utils/log-ssh-cmd))
    (utils/log "Unable to launch machinekit. No SSH connection")))

(defn shutdown-mk!
  []
  (utils/log "Shuting down machinekit")
  (if (-> @store/state :connection :connected?)
    (do
      (utils/log "Shutting down mk")
      (server-interop/send-data (utils/encode-buffer MT_SHUTDOWN) #(utils/log %)))
    "Unable to shutdown machinekit. Not connected"))

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

(def
  edit-callbacks {"ini" edit-ini!})

(defn edit-file!
  [full-filename]
  (assert (string? full-filename))
  (assert (string/includes? full-filename "machinekit"))
  (let [extension (utils/file-ext full-filename)
        callback (get edit-callbacks extension edit-unsupported)
        callback #(callback %1 [:remote full-filename])]
    (server-interop/sftp-get full-filename callback)))

(defn upload-file!
  [full-filename contents]
  (assert (string? full-filename))
  (assert (string/includes? full-filename "machinekit"))
  (let []
    (server-interop/sftp-put contents full-filename update-configs!)))

(defn download-file!
  [full-filename]
  (assert (string? full-filename))
  (assert (string/includes? full-filename "machinekit"))
  (let [fname (utils/fname-from-path full-filename)]
    (server-interop/sftp-get full-filename
                             #(utils/save-file %1 fname))))

(defn delete-file!
  [full-filename]
  {:pre [(string? full-filename)]}
  (server-interop/sftp-rm full-filename update-configs!))

(defn- --update-connection-status
  [state status]
  (let [{:keys [connected? hostname username]} status]
    (if connected?
      (update state :connection
              #(merge %1 {:connected? %2
                          :hostname %3
                          :username %4})
              connected? hostname username)
      (assoc-in state [:connection :connected?] false))))

(defn update-connection-status!
  []
  (server-interop/connection-status #(swap! store/state
                                            --update-connection-status %1)))

(utils/set-interval "update-connection-status!"
                    update-connection-status!
                    500)

(defn log-state
  "Adding whatever information you want to see for debugging here"
  []
  (utils/log (str "Services: " (:services @store/state))))

(defn debug-state
  [timeout]
  (utils/set-interval "debug-state" log-state timeout))

(debug-state 10000)
