(ns app.actions
  ""
  (:require [reagent.core :as r :refer [atom]]))

(defonce app-state (atom {:tab "Home"}))

(defn set-tab! [label]
  (swap! app-state assoc :tab label))
