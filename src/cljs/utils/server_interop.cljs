(ns utils.server-interop
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
  ssh."
  [contents filename]
  (assert (string? contents))
  (assert (string? filename))
  (remote-callback :sftp-put [contents filename] identity))

(defn sftp-get
  "Gets the contents of a file through sftp. It is required that the server is
  connected through ssh."
  [filename callback]
  (assert (string? filename))
  (assert (fn? callback))
  (remote-callback :sftp-get [filename] callback))

(defn sftp-ls
  "Runs the ls command over the remote ssh server. A vector of hash-maps with the
  keys :name and :type is passed to callback."
  [path callback]
  (remote-callback :sftp-ls [path] callback))

(defn connection-status
  "Gets the connection status of the server as a hashmap. See
  clj/server/client_interop.clj for more details."
  [callback]
  (remote-callback :connection-status [] callback))
