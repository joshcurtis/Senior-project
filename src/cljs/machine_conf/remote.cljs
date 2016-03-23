(ns machine-conf.remote
  (:require
   [shoreleave.remotes.http-rpc :refer [remote-callback]]
   [cljs.reader :refer [read-string]])
  (:require-macros [shoreleave.remotes.macros :as macros]))

;; See machine_serv/core.clj

(defn server-println
  [s]
  (let [s (str s)]
    (remote-callback :server-println [s] #(assert (= s %1)))))
