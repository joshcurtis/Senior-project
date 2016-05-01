(ns view.text-editor
  "Provides the view for the text editor."
  (:require
   [model.core :as model]
   [controller.text-editor :as controller]
   [utils.core :as utils]
   [widgets.core :as widgets]
   [widgets.navbar :as navbar]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(defn simple-text-editor
  [props]
  (let [{:keys [selected-id all-ids text]} props]
    (if (some? selected-id)
      [:div
       [widgets/infosection {:selected-id selected-id
                             :all-ids all-ids
                             :on-change-id controller/set-selected-id!}]
       [:pre
        [:textarea {:value text
                    :rows (->> text (re-seq #"\n") count inc)
                    :style {:resize "none"
                            :width "100%"
                            :height "100%"}
                    :on-change #(controller/change-current-text! (-> %1 .-target .-value))}]]])))
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
    [simple-text-editor {:selected-id selected-text-id
                         :all-ids (keys texts)
                         :text value}]))
