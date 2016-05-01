(ns controller.core
  "Misc. functionality such as switching the current tab or changing the theme."
  (:require [model.core :as model]))

(defn set-tab!
  [label]
  (swap! model/state assoc :tab label))

(defn change-theme! [theme]
  (swap! model/state assoc :current-theme theme)
  (js/changeStyle (str "css/bootstrap-" theme ".css")))

(defonce theme-changed?
  (do (change-theme! "metro")
      true))
