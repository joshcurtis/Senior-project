(ns text-editor.core
  "Provides functionality for editing ini files."
  (:require
   [app.store :as store]
   [text-editor.controller :as controller]
   [text-editor.view :as view]))

(def topbar-actions {"open" controller/load-text!
                     "save" store/text-str
                     "close" controller/close-selected!
                     "filename" controller/filename
                     "filename-filter" (constantly true)})

(defn contents
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [{:keys [texts selected-text-id]} @store/state
        value (get texts selected-text-id)]
    [view/simple-text-editor {:selected-id selected-text-id
                              :all-ids texts
                              :text value}]))

(defn text-as-str
  "Convert the ini data that is currently in the ini-editor/model into a text
  string."
  []
  (store/text-str))
