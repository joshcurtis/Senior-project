(ns ini-editor.core
  "Provides functionality for editing ini files."
  (:require
   [ini-editor.model :as model]
   [ini-editor.view :as view]))

(defn view
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  [:div {}
   [view/menubar {}]
   [view/ini-editor (merge @model/ini
                           {:expanded? @model/expanded?}
                           {:loaded? @model/loaded?})]])

(defn ini-as-str
  "Convert the ini data that is currently in the ini-editor/model into an ini
  string compatible with machinekit."
  []
  (model/ini-str))
