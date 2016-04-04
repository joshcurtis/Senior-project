(ns server.core
  "The server for our web application. It also provides RPC through the
  shoreleave library."
  (:require
   [server.client-interop :as client-interop]
   [server.state :as state]
   [server.mdns :as mdns]
   [compojure.core :refer [defroutes GET]]
   [compojure.handler :refer [site]]
   [compojure.route :refer [not-found files resources]]
   [hiccup.core :as hiccup]
   [shoreleave.middleware.rpc :refer [wrap-rpc]]
   [ring.util.response :as resp]
   ))

;; compojure ring handler
(defroutes static-handler
  "Compojure route handler.
  1. Redirects ./ to ./index.html
  2. Points file requests to target folder
  3. Points resource requests to target folder
  4. Page Not Found route"
  (GET "/" [] (resp/redirect "index.html"))
  (files "/" {:root "target"})
  (resources "/" {:root "target"})
  (not-found "Page Not Found"))

;; also a ring handler, but adds shoreleave functionality
(def handler
  "compojure route handler. This is similar to handler, but adds in
  functionality from shoreleave which allows for easy Remote Procedure Calls
  from cljs."
  (-> (var static-handler) (wrap-rpc) (site)))
