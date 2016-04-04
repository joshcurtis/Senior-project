(ns text-editor.view
  "Provides the view for the INI editor."
  (:require
   [text-editor.controller :as controller]
   [utils.core :as utils]
   [utils.widgets :as widgets]
   [utils.navbar :as navbar]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(defn simple-text-editor
  [props]
  (let [{:keys [selected-id all-ids text]} props]
    (assert (string? text))
    (assert (some? selected-id))
    [:div
     [widgets/infosection {:selected-id selected-id
                           :all-ids all-ids
                           :on-change-id controller/set-selected-id!}]
     [:textarea {:value text
                 :rows (->> text (re-seq #"\n") count inc)
                 :style {:resize "none"
                         :width "100%"
                         :height "100%"}
                 :on-change #(controller/change-current-text (-> %1 .-target .-value))}]]))
