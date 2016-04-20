(ns bbserver.core
  (:require-macros
   [cljs.core.async.macros :refer [go]])
  (:require
   [clojure.string :as string]
   [cljs.reader :as reader]
   [cljs-http.client :as http]
   [cljs.core.async :refer [<!]]))

(defn- bb-wrapper
  ([http-type address callback options]
   (go (let [options (merge {:with-credentials? false} options)
             resp (<! (http-type address
                                 options))]
         (-> resp :body reader/read-string callback))))
  ([http-type address callback] (bb-wrapper http-type address callback {})))

(defn- bb-get
  [address callback]
  (bb-wrapper http/get address callback))

(defn- bb-put
  [address callback options]
  (js/alert (str options))
  (bb-wrapper http/put address callback options))

(defn- bb-delete
  [address callback]
  (bb-wrapper http/delete address callback))

(defn- build-address
  [hostname port route]
  (str "http://" hostname \: port route))

(defn status
  [hostname callback]
  (bb-get (build-address hostname 3001 "/status") callback))

(defn configs
  [hostname callback]
  (bb-get (build-address hostname 3001 "/configs") callback))

(defn get-file
  [hostname config filename callback]
  {:pre [(string/includes? config \/)]}
  (bb-get (build-address hostname 3001 (str "/configs/" config filename)) callback))

(defn put-file
  [hostname config filename contents callback]
  {:pre [(string/includes? config \/) (string? contents)]}
  (bb-put (build-address hostname 3001 (str "/configs/" config filename))
          callback {:form-params {:contents contents}}))

(defn delete-file
  [hostname config filename callback]
  {:pre [(string/includes? config \/)]}
  (bb-delete (build-address hostname 3001 (str "/configs/" config filename)) callback))

(defn run_mk
  [hostname callback]
  (bb-get (build-address hostname 3001 "/run_mk") callback))

(defn stop_mk
  [hostname callback]
  (bb-get (build-address hostname 3001 "/stop_mk") callback))
