(ns monitor.core
  "Shows some measurements."
  (:require
   [monitor.view :as view]
   [app.store :as store]
   [monitor.controller :as controller]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(utils/set-interval "rand-update"
                    controller/update-measurements!
                    1000)

(defn contents
  [props]
  (if @(r/cursor store/state [:connection :connected?])
    [view/contents-active]
    [view/contents-inactive]))
