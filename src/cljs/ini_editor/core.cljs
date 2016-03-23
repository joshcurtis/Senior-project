(ns ini-editor.core
  "Provides functionality for editing ini files."
  (:require
   [ini-editor.model :as model]
   [ini-editor.view :as view]))

(defn view
  "A view that can be rendered to edit the current ini file. See
  machine_conf/core.cljs. This returns a reagent component that takes no props."
  [props]
  [:div {}
   [view/menubar {}]
   [view/ini-editor {:key-metadata @model/key-metadata
                     :key-order @model/key-order
                     :section-metadata @model/section-metadata
                     :section-order @model/section-order
                     :values @model/values
                     :expanded? @model/expanded?}]])

(defn ini-as-str
  "Convert the ini data that is currently in the ini-editor/model into an ini
  string compatible with machinekit."
  []
  (model/ini-str))
