(ns remote-manager.controller
  (:require
   [utils.core :as utils]
   [remote-manager.model :as model]
   [ini-editor.controller]
   [text-editor.controller]
   [server-interop.core :as server-interop]
   [clojure.string :as string]))

(defonce pi 3.14)
(defonce mt (.-protobuf js/machinetalk))
(defonce container (.-Container (.-message mt)))
(defonce container-types (.-ContainerType (.-message mt)))
(defonce MT_PING (.-MT_PING container-types))
(defonce MT_SHUTDOWN (.-MT_SHUTDOWN container-types))

(defn encode-buffer
  "TODO: Move to utils
   Handle more data"
  [type]
  (let [encoded (.encode container type)
        limit (.-limit encoded)
        buffer (.-view encoded)
        sliced (map #(aget buffer %) (range 3))]
    sliced))

(defn set-hostname!
  [name]
  (swap! model/connection assoc :hostname name))

(defn set-username!
  [name]
  (swap! model/connection assoc :username name))

(defn set-password!
  [password]
  (swap! model/connection assoc :password password))

(defn clear-configs!
  []
  (reset! model/configs {:dirs [] :contents {}}))

(defn update-config!
  "Updates a single config based on the server. There isn't really a reason to
  call this function. Prefer to use `update-configs!`, which uses this function."
  [dir]
  (assert (utils/dir? dir))
  (let [callback (fn [files]
                   (swap! model/configs assoc-in [:contents dir] files))]
    (server-interop/sftp-ls (str "machinekit/configs/" dir) callback)))

(defn update-configs!
  "Updates the configs based on the server."
  []
  (let [valid-dir #(or (utils/dir? %) (= "" %))
        callback (fn [dirs]
                   (swap! model/configs assoc :dirs (filterv valid-dir (into [""] dirs)))
                   (swap! model/configs assoc-in [:contents ""] (filterv (complement utils/dir?) dirs))
                   (doseq [dir dirs] (if (valid-dir dir) (update-config! dir))))]
    (clear-configs!)
    (server-interop/sftp-ls "machinekit/configs/" callback)))

(utils/set-interval "update-configs-when-empty"
                    #(if (and (:connected? @model/connection)
                              (empty? (:dirs @model/configs)))
                       (update-configs!))
                    2000)

(defn connect!
  []
  (let [{:keys [hostname username password]} @model/connection
        callback (fn [res] (if (nil? res) (do
                                            (swap! model/connection assoc
                                                   :connected? true
                                                   :connection-pending? false
                                                   :error nil)
                                            (update-configs!))
                             (swap! model/connection assoc
                                    :connected? false
                                    :connection-pending? false
                                    :error res)))]
    (swap! model/connection assoc
           :connection-pending? true
           :error nil)
    (server-interop/ssh-connect! hostname username password callback)))

(defn disconnect!
  []
  (server-interop/ssh-disconnect!)
  (swap! model/connection assoc
         :connected? false
         :connection-pending? false
         :error nil))

(defn- log-ssh-cmd
  "Output should be a map with the keys :exit, :err, and :out.
  :exit contains the return code
  :err contains stderr
  :out contains stdout"
  [output]
  (let [{:keys [exit err out]} output]
    (if (some? err)
      (.log js/console err))
    (if (some? out)
      (.log js/console out))))

(defn parse-service
  "Takes a line of the form
   str service address
   and returns a vector containing the service and address.

   The address should have the form
   protocol:ip_address:port

   Example Usage:
   (parse-resolved-service resolved command tcp://beaglebone.local:53123) ->
   [:command \"53123\"]"
  [line]
  (let [[_ service address] (string/split line " ")
        [_ _ port] (string/split address ":")]
    [(keyword service) port]))

(defn parse-resolve-log
  "Takes a log of output from the resolve.py script
  and returns a dictionary of available services with their ports"
  [log]
  (let [lines (->> log string/split-lines (map string/trim))
        services-lines (filter #(string/starts-with? % "resolved") lines)
        service-ports (mapcat parse-service services-lines)]
    (apply hash-map service-ports)))


(defn update-services!
  [log]
  (println "Updating")
  (println log)
  (println (parse-resolve-log log))
  (reset! model/services (parse-resolve-log log)))

(defn update-mk-services!
  "Run to parse the ~/Desktop/services.log file
  and update what machinekit services are available"
  []
  (server-interop/get-service-log update-services!))

(defn- print-available-services
  []
  (println @model/services))

(defn- log-available-services
  []
  (.log js/console "Services: ")
  (.log js/console (str @model/services)))

(defn launch-mk!
  []
  (.log js/console "Trying to launch machinekit")
  (if (:connected? @model/connection)
    (do
      (server-interop/watch-mk-services! log-ssh-cmd)
      (server-interop/launch-mk! log-ssh-cmd)
      (utils/set-interval "update-services" update-mk-services! 2000)
      (utils/set-interval "log-available-services" log-available-services 2000))
    "Unable to launch machinekit. No SSH connection"))

(defn shutdown-mk!
  []
  (.log js/console "Shuting down machinekit")
  (if (:connected? @model/connection)
    (do
      (.log js/console "Shutting down mk")
      (server-interop/send-data (encode-buffer MT_PING) #(.log js/console %)))
    "Unable to shutdown machinekit. Not connected"))

(defn test-socket
  []
  (.log js/console "Testing socket")
  (server-interop/send-data (encode-buffer MT_PING) #(.log js/console %)))

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


(defn- update-c-status-helper!
  [status]
  (let [{:keys [connected? hostname username]} status]
    (if connected?
      (swap! model/connection assoc
             :connected? connected?
             :hostname hostname
             :username username)
      (swap! model/connection assoc
             :connected? connected?))))

(defn update-connection-status!
  []
  (server-interop/connection-status update-c-status-helper!))

(utils/set-interval "update-connection-status!"
                    update-connection-status!
                    2000)
