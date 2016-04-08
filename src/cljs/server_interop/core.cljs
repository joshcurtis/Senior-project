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

(defn sftp-put
  "Puts a file through sftp. It is required that the server is connected through
  ssh. If callback is not nil, then it will be called upon completion."
  [contents filename callback]
  (assert (string? contents))
  (assert (string? filename))
  (remote-callback :sftp-put [contents filename] (or callback identity)))

(defn sftp-get
  "Gets the contents of a file through sftp. It is required that the server is
  connected through ssh."
  [filename callback]
  (assert (string? filename))
  (assert (fn? callback))
  (remote-callback :sftp-get [filename] callback))

(defn get-service-log
  [callback]
  (remote-callback :get-service-log [] callback))

(defn sftp-ls
  "Runs the ls command over the remote ssh server. A vector of hash-maps with the
  keys :name and :type is passed to callback."
  [path callback]
  (remote-callback :sftp-ls [path] callback))

(defn sftp-rm
  "Runs the ls command over the remote ssh server. If callback is a not nil,
  then it will be called once the action is complete."
  [path callback]
  (remote-callback :sftp-rm [path] (or callback identity)))

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
  (run-ssh-command  "sh ~/Desktop/launch.sh" callback))

(defn cleanup!
  "Cleanup running processes"
  [callback]
  (run-ssh-command "sh ~/Desktop/cleanup.sh" callback))

(defn send-data
  "Function for quickly testing zmq functionality"
  [data callback]
  (remote-callback :send-data [data] callback))
