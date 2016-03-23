(ns machine-conf.remote
  "Functions that run on the Clojure server. See also machine_serve/remote.clj
  for the server side implementation."
  (:require
   [shoreleave.remotes.http-rpc :refer [remote-callback]]
   [cljs.reader :refer [read-string]])
  (:require-macros [shoreleave.remotes.macros :as macros]))

(defn sftp-put
  "Write the contents to a string to a remote ssh server with the given
  hostname. The username that is used is \"machinekit\" and the password is also
  \"machinekit\".
  hostname - hostname-string, usually ip address
  contents - string that will be written to the remote file.
  filename - string representing where to put file."
  [hostname contents filename]
  (assert (string? hostname))
  (assert (string? contents))
  (assert (string? filename))
  (remote-callback :sftp-put [hostname contents filename] identity))

(defn sftp-get
  "Read the contents of a remote file to string. This call is asynchronous. This
  is surprisingly slow."
  [hostname filename callback]
  (assert (string? hostname))
  (assert (string? filename))
  (assert (fn? callback))
  (remote-callback :sftp-get [hostname filename] callback))
