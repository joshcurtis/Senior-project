(ns ini-editor.core
  "Provides functionality for editing ini files."
  (:require
   [app.store :as store]
   [ini-editor.controller :as controller]
   [ini-editor.view :as view]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(def topbar-actions {"open" controller/load-str!
                     "save" controller/ini-str
                     "close" controller/close-selected!
                     "filename" controller/filename
                     "filename-filter" #(string/ends-with? %1 ".ini")})

(defn contents
  "A view that can be rendered to edit the current ini file. It is used in
  app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [inis @(r/cursor store/state [:inis])
        selected-ini-id @(r/cursor store/state [:selected-ini-id])
        all-ids (keys inis)
        model-ini (get inis selected-ini-id)]
    [view/ini-editor (merge (:ini model-ini)
                            {:expanded? (:expanded? model-ini)
                             :all-ids all-ids
                             :selected-id selected-ini-id})]))

(defn ini-as-str
  "Convert the ini data that is currently in the ini-editor/model into an ini
  string compatible with machinekit."
  []
  (store/ini-str))
