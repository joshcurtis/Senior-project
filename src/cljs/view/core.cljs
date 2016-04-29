(ns view.core
  (:require
   [model.core :as model]
   [view.home]
   [view.ini-editor]
   [view.monitor]
   [view.remote]
   [view.text-editor]
   [reagent.core :as r]))

(def home view.home/contents)
(def remote view.remote/contents)
(def monitor view.monitor/contents)
(def ini view.ini-editor/contents)
(def text view.text-editor/contents)

(defn app-view
  ""
  []
  (let [tab @(r/cursor model/state [:tab])]
    [:div.tab.content.panel.panel-default
      [:div.panel-body
        [:div.tab-pane.active {}
          (case tab
            "Home" [home {}]
            "Remote" [remote {}]
            "Monitor" [monitor {}]
            "INI" [ini {}]
            "Text" [text {}]
          )]]]))
