(ns text-editor.core
  "Provides functionality for editing ini files."
  (:require
   [text-editor.model :as model]
   [text-editor.controller :as controller]
   [text-editor.view :as view]))

(def topbar-actions {"open" controller/load-text!
                     "save" model/text-str
                     "close" controller/close-selected!
                     "filename" "scratch.txt"
                     "filename-filter" (constantly true)})

(defn contents
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  [view/simple-text-editor {:selected-id @model/selected-id
                            :all-ids (keys @model/texts)
                            :text (model/text-str)}])

(defn text-as-str
  "Convert the ini data that is currently in the ini-editor/model into a text
  string."
  []
  (model/text-str))
