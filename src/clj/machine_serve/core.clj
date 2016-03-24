(ns machine-serve.core
  "The server for our web application. It also provides RPC through the
  shoreleave library."
  (:require
   [machine-serve.client-interop :as client-interop]
   [machine-serve.state :as state]
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

(defroutes handler
  "compojure route handler."
  (GET "/" [] home)
  (files "/" {:root "target"})
  (resources "/" {:root "target"})
  (GET "/echo/:s" [s] s)
  (not-found "Page Not Found"))


(def app
  "compojure route handler. This is similar to handler, but adds in
  functionality from shoreleave which allows for easy Remote Procedure Calls
  from cljs."
  (-> (var handler) (wrap-rpc) (site)))
