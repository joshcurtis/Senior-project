(ns text-editor.core
  "Provides functionality for editing ini files."
  (:require
   [model.core :as model]
   [text-editor.controller :as controller]
   [text-editor.view :as view]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {"open" controller/load-text!
                     "save" model/text-str
                     "close" controller/close-selected!
                     "filename" controller/filename
                     "filename-filter" (constantly true)})

(defn contents
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [texts @(r/cursor model/state [:texts])
        selected-text-id @(r/cursor model/state [:selected-text-id])
        value (get texts selected-text-id)]
    [view/simple-text-editor {:selected-id selected-text-id
                              :all-ids (keys texts)
                              :text value}]))

(defn text-as-str
  "Convert the ini data that is currently in the ini-editor/model into a text
  string."
  []
  (model/text-str))
