(ns app.store
  "The application state, this should've been it in the beginning."
  (:require
   [ini-editor.parser :as parser]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(def empty-sequence
  "A sequence is a data structure that can conj a new item. This will be
  converted into a js-array.  Why is this a constant? You can use a list, and
  new items will be put at the front and the plot will be drawn backwards. You
  can use a vector, and items will be pushed back. I don't know which is more
  efficient considering memory, conj'ing, and js conversion."
  [])


(defonce state
  (atom
   {

    ;;; Tab and theme stuff
    :tab "Home"
    :tab-labels ["Home" "Remote" "Monitor" "INI" "Text"]
    :current-theme "flatly"

    ;;; Remote Manager Stuff
    :connection {:connected? false
                 :connection-pending? false
                 :error nil
                 :hostname "beaglebone.local"
                 :username "machinekit"
                 :password ""}
    :configs {:dirs []
              :contents {}}

    :services {}
    :sockets {}

    ;;; INI Stuff
    ;; hashmap: [source path] -> {:ini parsed-ini from ini-editor.parser
    ;;                            :expanded? #{section-strings}}
    :inis {}

    ;; [source path]
    :selected-ini-id nil

    ;;; Text Editor Stuff
    ;; hashmap: [source path] -> string
    :texts {[:local "scratch.txt"] ""}
    :selected-text-id [:local "scratch.txt"]

    ;;; Monitor Stuff
    ;; seconds
    :initial-time (utils/time-seconds)
    :is-monitoring? false
    :monitor {
              :all-components ["Axis-0-x" "Axis-0-y" "Axis-0-z" "Axis-0-a"
                               "Axis-1-x" "Axis-1-y" "Axis-1-z" "Axis-1-a"
                               "Axis-2-x" "Axis-2-y" "Axis-2-z" "Axis-2-a"
                               "Axis-3-x" "Axis-3-y" "Axis-3-z" "Axis-3-a"
                               "Ext-0" "Ext-1" "Ext-2"]
              :measurements {"t" nil
                             "Axis-0-x" nil
                             "Axis-0-y" nil
                             "Axis-0-z" nil
                             "Axis-0-a" nil
                             "Axis-1-x" nil
                             "Axis-1-y" nil
                             "Axis-1-z" nil
                             "Axis-1-a" nil
                             "Axis-2-x" nil
                             "Axis-2-y" nil
                             "Axis-2-z" nil
                             "Axis-2-a" nil
                             "Axis-3-x" nil
                             "Axis-3-y" nil
                             "Axis-3-z" nil
                             "Axis-3-a" nil
                             "Ext-0" nil
                             "Ext-1" nil
                             "Ext-2" nil}
              :history {"t" empty-sequence
                        "Axis-0-x" empty-sequence
                        "Axis-0-y" empty-sequence
                        "Axis-0-z" empty-sequence
                        "Axis-0-a" empty-sequence
                        "Axis-1-x" empty-sequence
                        "Axis-1-y" empty-sequence
                        "Axis-1-z" empty-sequence
                        "Axis-1-a" empty-sequence
                        "Axis-2-x" empty-sequence
                        "Axis-2-y" empty-sequence
                        "Axis-2-z" empty-sequence
                        "Axis-2-a" empty-sequence
                        "Axis-3-x" empty-sequence
                        "Axis-3-y" empty-sequence
                        "Axis-3-z" empty-sequence
                        "Axis-3-a" empty-sequence
                        "Ext-0" empty-sequence
                        "Ext-1" empty-sequence
                        "Ext-2" empty-sequence}
              :groups {:temperatures ["Ext-0" "Ext-1" "Ext-2"]
                       :axes (mapv #(let [name (str "Axis-" %1)]
                                      {:name name
                                       :x (str name "-x")
                                       :y (str name "-y")
                                       :z (str name "-z")})
                                   (range 4))}}
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

;; text editor helpers
(defn text-str
  "Convert the current model into an string. nil is returned if there is no text
  that is currently being edited."
  []
  (let [{:keys [texts selected-text-id]} @state]
    (get texts selected-text-id)))
