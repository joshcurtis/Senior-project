(ns text-editor.core
  "Provides functionality for editing ini files."
  (:require
   [app.store :as store]
   [text-editor.controller :as controller]
   [text-editor.view :as view]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {"open" controller/load-text!
                     "save" store/text-str
                     "close" controller/close-selected!
                     "filename" controller/filename
                     "filename-filter" (constantly true)})

(defn contents
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [texts @(r/cursor store/state [:texts])
        selected-text-id @(r/cursor store/state [:selected-text-id])
        value (get texts selected-text-id)]
    [view/simple-text-editor {:selected-id selected-text-id
                              :all-ids (keys texts)
                              :text value}]))

(defn text-as-str
  "Convert the ini data that is currently in the ini-editor/model into a text
  string."
  []
  (store/text-str))
