(ns view.core
  "Contains `app-view` which provides a view to the application. The views for
  each tab are specified in the other namespaces under `view`."
  (:require
   [model.core :as model]
   [view.home]
   [view.ini-editor]
   [view.jobs]
   [view.monitor]
   [view.remote]
   [view.text-editor]
   [reagent.core :as r]))

(def home view.home/contents)
(def remote view.remote/contents)
(def jobs view.jobs/contents) ;; not included, MachineKit backend not ready
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
            "Jobs" [jobs {}]
            "Monitor" [monitor {}]
            "INI" [ini {}]
            "Text" [text {}]
          )]]]))
