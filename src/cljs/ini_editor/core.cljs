(ns ini-editor.core
  "Edit MachineKit INI Files."
  (:require
   [ini-editor.model :as model]
   [ini-editor.view :as view]))

(defn view
  ""
  [props]
  [:div {}
   [view/menubar {}]
   [view/ini-editor {:key-metadata @model/key-metadata
                     :key-order @model/key-order
                     :section-metadata @model/section-metadata
                     :section-order @model/section-order
                     :values @model/values
                     :expanded? @model/expanded?}]])

(defn ini-as-str []
  (model/ini-str))
