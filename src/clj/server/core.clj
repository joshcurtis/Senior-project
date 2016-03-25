(ns server.core
  "The server for our web application. It also provides RPC through the
  shoreleave library."
  (:require
   [server.client-interop :as client-interop]
   [server.state :as state]
   [compojure.core :refer [defroutes GET]]
   [compojure.handler :refer [site]]
   [compojure.route :refer [not-found files resources]]
   [hiccup.core :as hiccup]
   [shoreleave.middleware.rpc :refer [wrap-rpc]]
   ))


(def home
  "HTML page (string) that is returned for /."
  (hiccup/html [:html  [:body
                             [:h1 {} "Welcome To MachineKit Config"]
                             [:a {:href "/index.html"} "here"]]]))

;; compojure ring handler
(defroutes static-handler
  "compojure route handler."
  (GET "/" [] home)
  (files "/" {:root "target"})
  (resources "/" {:root "target"})
  (GET "/echo/:s" [s] s)
  (not-found "Page Not Found"))

;; also a ring handler, but adds shoreleave functionality
(def handler
  "compojure route handler. This is similar to handler, but adds in
  functionality from shoreleave which allows for easy Remote Procedure Calls
  from cljs."
  (-> (var static-handler) (wrap-rpc) (site)))
