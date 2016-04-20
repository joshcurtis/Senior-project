(ns server-interop.core
  "Functions that run on the Clojure server. See clj/server/client_interop.clj
  for the server side implementation."
  (:require
   [utils.core :as utils]
   [shoreleave.remotes.http-rpc :refer [remote-callback]]
   [cljs.reader :refer [read-string]])
  (:require-macros [shoreleave.remotes.macros :as macros]))

(defn ssh-disconnect!
  "Disconnects from the remote device. If the device was not connected, nothing
  happens."
  []
  (remote-callback :ssh-disconnect! [] identity))

(defn ssh-connect!
  "Connect to a remote device with ssh. nil is passed to the callback if the
  connection was successful and an error string otherwise."
  [hostname username password callback]
  (assert (string? hostname))
  (assert (string? username))
  (assert (string? password))
  (remote-callback :ssh-connect! [hostname username password] callback))

(defn connection-status
  "Gets the connection status of the server as a hashmap. See
  clj/server/client_interop.clj for more details."
  [callback]
  (remote-callback :connection-status [] callback))

(defn- run-ssh-command
  [command callback]
  (remote-callback :run-ssh-command [command] callback))

(defn watch-mk-services!
  "Watch mk"
  [callback]
  (run-ssh-command "sh ~/Desktop/resolve.sh" callback))

(defn launch-mk!
  "Launch machinekit"
  [callback]
  (run-ssh-command "sh ~/Desktop/launch.sh" callback))

(defn cleanup!
  "Cleanup running processes"
  [callback]
  (run-ssh-command "sh ~/Desktop/cleanup.sh" callback))

(defn send-data
  "Function for quickly testing zmq functionality"
  [data callback]
  (remote-callback :send-data [data] callback))
