(ns bbserver.core
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [cljs.reader :as reader]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]))

(defn- bb-get
  [address callback]
  (go (let [resp (<! (http/get address
                               {:with-credentials? false}))]
        (-> resp :body reader/read-string callback))))

(defn- build-address
  [address port route]
  (str "http://" address \: port route))

(defn status
  [address callback]
  (bb-get (build-address address 3001 "/status") callback))

(defn configs
  [address callback]
  (bb-get (build-address address 3001 "/configs") callback))

(defn run_mk
  [address callback]
  (bb-get (build-address address 3001 "/run_mk") callback))

(defn stop_mk
  [address callback]
  (bb-get (build-address address 3001 "/stop_mk") callback))
