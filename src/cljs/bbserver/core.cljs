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
   (go (let [options (merge {:with-credentials? false :timeout 2000} options)
             resp (<! (http-type address
                                 options))
             {:keys [status error-code body]} resp
             body (reader/read-string body)]
         ; TODO Modify callbacks to take status + error-code + body
         (-> resp :body reader/read-string callback))))
  ([http-type address callback] (bb-wrapper http-type address callback {})))

(defn- bb-get
  ([address callback]
  (bb-get address callback {}))
  ([address callback options]
  (bb-wrapper http/get address callback options)))

(defn- bb-put
  [address callback options]
  (bb-wrapper http/put address callback options))

(defn- bb-post
  [address callback options]
  (bb-wrapper http/post address callback options))

(defn- bb-delete
  ([address callback]
  (bb-delete address callback {}))
  ([address callback options]
  (bb-wrapper http/delete address callback options)))

(defn- build-address
  [hostname port route]
  (str "http://" hostname \: port route))

(defn status
  [hostname callback]
  (bb-get (build-address hostname 3001 "/status") callback))

(defn login
  [hostname password callback]
  (bb-post (build-address hostname 3001 "/login") callback
    {:form-params {:password password}}))

(defn configs
  [hostname callback]
  (bb-get (build-address hostname 3001 "/configs") callback))

(defn get-file
  [hostname config filename callback]
  {:pre [(string/includes? config \/)]}
  (bb-get (build-address hostname 3001 "/config") callback
    {:query-params {:path (str config filename)}}))

(defn put-file
  [hostname config filename contents callback]
  {:pre [(string/includes? config \/) (string? contents)]}
  (bb-put (build-address hostname 3001 "/config") callback
    {:query-params {:path (str config filename)}
     :form-params {:contents contents}}))

(defn delete-file
  [hostname config filename callback]
  {:pre [(string/includes? config \/)]}
  (bb-delete (build-address hostname 3001 "/config") callback
    {:query-params {:path (str config filename)}}))

(defn get-services-log
  [hostname callback]
  (bb-get (build-address hostname 3001 "/services_log") callback))

(defn resolve-services
  [hostname callback]
  (bb-get (build-address hostname 3001 "/resolve") callback))

(defn run_mk
  [hostname callback]
  (bb-get (build-address hostname 3001 "/run_mk") callback))

(defn stop_mk
  [hostname callback]
  (bb-get (build-address hostname 3001 "/stop_mk") callback))

(defn ping
  [hostname port callback]
  (bb-get (build-address hostname 3001 (str "/ping/" port)) callback))
