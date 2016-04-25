(ns server-interop.core
  "Functions that run on the Clojure server. See clj/server/client_interop.clj
  for the server side implementation."
  (:require
   [utils.core :as utils]
   [shoreleave.remotes.http-rpc :refer [remote-callback]]
   [cljs.reader :refer [read-string]])
  (:require-macros [shoreleave.remotes.macros :as macros]))

(defn connection-status
  "Gets the connection status of the server as a hashmap. See
  clj/server/client_interop.clj for more details."
  [callback]
  (remote-callback :connection-status [] callback))

;; todo

(defn watch-mk-services!
  "Watch mk"
  [callback]
  (run-ssh-command "sh ~/Desktop/resolve.sh" callback))

(defn cleanup!
  "Cleanup running processes"
  [callback]
  (run-ssh-command "sh ~/Desktop/cleanup.sh" callback))

(defn send-data
  "Function for quickly testing zmq functionality"
  [data callback]
  (remote-callback :send-data [data] callback))
