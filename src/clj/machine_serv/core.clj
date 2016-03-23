(ns machine-serv.core
  (:require
   [compojure.core :refer [defroutes GET]]
   [compojure.handler :refer [site]]
   [compojure.route :refer [not-found files resources]]
   [hiccup.core :as hiccup]
   [shoreleave.middleware.rpc :refer [defremote wrap-rpc]]
   ))


(def home (hiccup/html [:html  [:body
                                [:h1 {} "Welcome To MachineKit Config"]
                                [:a {:href "/index.html"} "here"]]]))

(defroutes handler
  (GET "/" [] home)
  (files "/" {:root "target"})
  (resources "/" {:root "target"})
  (GET "/echo/:s" [s] s)
  (not-found "Page Not Found"))

;; see machine_conf/core.cljs

(defremote server-println [s]
  (println s)
  s)

(def app (-> (var handler)
             (wrap-rpc)
             (site)))
