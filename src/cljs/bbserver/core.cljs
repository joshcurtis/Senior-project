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
        (.log js/console resp)
        (-> resp :body reader/read-string callback))))

(defn- build-address
  [hostname port route]
  (str "http://" hostname \: port route))

(defn status
  [hostname callback]
  (bb-get (build-address hostname 3001 "/status") callback))

(defn configs
  [hostname callback]
  (bb-get (build-address hostname 3001 "/configs") callback))

(defn run_mk
  [hostname callback]
  (bb-get (build-address hostname 3001 "/run_mk") callback))

(defn stop_mk
  [hostname callback]
  (bb-get (build-address hostname 3001 "/stop_mk") callback))
