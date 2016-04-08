(ns app.store
  "The application state, this should've been it in the beginning."
  (:require
   [ini-editor.parser :as parser]
   [reagent.core :as r :refer [atom]]))

(defonce state
  (atom
   {

    ;; INI Stuff
    ;; hashmap: [source path] -> {:ini parsed-ini from ini-editor.parser
    ;;                            :expanded? #{section-strings}}
    :inis {}

    ;; [source path]
    :selected-ini-id nil

    }))

;; global helpers

;; ini helpers

(defn update-selected-ini!
  [f & args]
  (swap! state
         (fn [state]
           (let [{:keys [inis selected-ini-id]} state
                 ini (get inis selected-ini-id)]
             (update-in state [:inis selected-ini-id]
                        #(apply f (concat [ini] args)))))))

(defn ini-str
  "Get the currently selected INI as a string."
  []
  (let [{:keys [inis selected-ini-id]} @state
        ini (get inis selected-ini-id)]
    (if (some? ini)
      (ini-editor.parser/ini-to-str ini)
      (js/alert "No INI has been loaded!"))))
