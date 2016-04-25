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

(defn add-socket!
  "type should be one of :dealer or :subscriber
  TODO: Possibly choose the type based on the service"
  [hostname service port type]
  (let [endpoint (str "tcp://" hostname ":" port)
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
