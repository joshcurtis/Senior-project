(ns server.client-interop
  "Server side functions that can be called from clojurescript. See
  cljs/server_interop/core.cljs for the cljs interface."
  (:require
   [server.state :as state]
   [clojure.string :as string]
   [clj-ssh.ssh :as ssh]
   [zeromq.zmq :as zmq]
   [shoreleave.middleware.rpc :refer [defremote]])
  (:import
   [org.apache.commons.codec.binary Hex]))

(defonce SSH-NO-SUCH-FILE-ERROR 2)

(defn add-socket!
  "type should be one of :dealer or :subscriber
  TODO: Possibly choose the type based on the service"
  [service port type]
  (let [endpoint (str "tcp://" (:hostname @state/connection-state) ":" port)
        context (zmq/zcontext)
        identity (.getBytes "machinekit-client")
        socket (doto  (zmq/socket context :dealer)
                 (zmq/connect endpoint))]
    (zmq/set-identity socket identity)
    (swap! state/sockets assoc service socket)))

(defn remove-socket!
  [service]
  (zmq/close (service @state/sockets))
  (swap! state/sockets dissoc service))

(defremote ssh-disconnect!
  "Disconnects from SSH. Returns nil if successful, otherwise error message."
  []
  (try
    (state/ssh-disconnect!)
    nil
  (catch Exception e (.getMessage e))))

(defremote ssh-connect!
  "Connect remotely through SSH. Returns nil if successful, otherwise error
  message."
  [hostname username password]
  (try
    (state/ssh-connect! hostname username password)
    nil
  (catch Exception e (.getMessage e))))

(defremote connection-status
  "Get the connection status of the server. This involves things such as being
  connected, username, hostname, and etc... See source code for more details."
  []
  (select-keys @state/connection-state [:connected?
                                        :hostname
                                        :username]))
(defremote run-ssh-command
  "Runs the cmd passed as a string.
   It returns the exit code, stdout, and stderr"
  [cmd]
  (locking state/ssh-lock
    (ssh/ssh (:session @state/connection-state) {:cmd cmd})))

(defn format-data
  [data]
  (let [hexstr (string/join (map #(format "%02X" %) data))]
    (Hex/decodeHex (.toCharArray hexstr))))

(defremote send-data
  "Socket testing functionality"
  [service data]
  (let [buffer (format-data data)
        socket (service @state/sockets)]
    (zmq/send socket buffer) data))
