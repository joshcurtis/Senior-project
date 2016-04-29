(ns monitor.core
  "Shows some measurements."
  (:require
   [model.core :as model]
   [monitor.view :as view]
   [monitor.controller :as controller]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(utils/set-interval "rand-update"
                    controller/update-measurements!
                    1000)

(defn contents
  [props]
  (if @(r/cursor model/state [:connection :connected?])
    [view/contents-active]
    [view/contents-inactive]))
