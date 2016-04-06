(ns app.topbar
  (:require
   [app.state :as state]
   [remote-manager.model]
   [remote-manager.controller]
   [text-editor.core]
   [utils.core :as utils]
   [utils.widgets :as widgets]
   [utils.navbar :as navbar]
   [reagent.core :as r :refer [atom]]))


;; topbar

(defonce current-modal (atom nil))

(defn close-modal! []
  (reset! current-modal nil))

(defn alert-invalid-action
  []
  (js/alert "Invalid Action")
  "")

; topbar-actions may define these functions:
;; "save" -> returns a string
;; "filename" -> returns a string
;; "filename-filter" -> returns true if mimetype is right for filename, ie *.ini
;; "open" -> takes an id and a string
(def topbar-actions-map {"Home" nil
                         "Remote" nil
                         "Monitor" nil
                         "INI" ini-editor.core/topbar-actions
                         "Text" text-editor.core/topbar-actions})

(defn current-topbar-actions
  []
  (get topbar-actions-map (:tab @state/app-state)))

(defn topbar-action-open
  ""
  [id string]
  (let [f (get (current-topbar-actions) "open")]
    (if (fn? f)
      (f id string)
      (alert-invalid-action))))

(defn topbar-action-save
  "Returns a string."
  []
  ((get (current-topbar-actions) "save" alert-invalid-action)))

(defn topbar-action-filename
  "Returns a string."
  []
  ((get (current-topbar-actions) "filename" alert-invalid-action)))

(defn topbar-action-filename-filter
  "Returns true if the string has proper name."
  [s]
  ((get (current-topbar-actions) "filename-filter" (constantly true)) s))

(defn topbar-action-close
  "Calls the close function."
  []
  ((get (current-topbar-actions) "close" identity)))

(defn topbar-file-menu
  ""
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        open-el [widgets/file-input {:id "file-input"
                                     :file-types nil
                                     :element "Open"
                                     :on-change (fn [file-list]
                                                  (let [file (first file-list)
                                                        f-id [:local (.-name file)]]
                                                    (if (some? file)
                                                      (utils/read-file file
                                                                       #(topbar-action-open f-id
                                                                                            %1)))))}]
        download-el [widgets/file-save {:element "Download"
                                        :filename topbar-action-filename
                                        :str-func topbar-action-save}]
        close-el [:a {:key "close"
                      :on-click topbar-action-close}
                  "Close"]
        dropdowns [
                   (if (contains? valid-options "save") open-el)
                   (if (and (contains? valid-options "save")
                            (contains? valid-options "filename"))
                     download-el)
                   (if (contains? valid-options "close")
                     close-el)
                   ]
        should-show? (some some? dropdowns) ; only show if there is a non-nil in dropdowns
        ]
    (if should-show?
      [navbar/navbar-dropdown {:title "File"
                               :key "file"
                               :labels dropdowns}])))

(defn topbar-remote-menu
  ""
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        open-el [:span {:key "open"
                        :on-click #(reset! current-modal "remote-open")} "Open"]
        upload-el [:span {:key "upload"
                          :on-click #(reset! current-modal "remote-save")} "Upload"]
        dropdowns [
                   (if (contains? valid-options "save") open-el)
                   (if (and (contains? valid-options "save")
                            (contains? valid-options "filename"))
                     upload-el)
                   ]
        should-show? (some some? dropdowns) ; only show if there is a non-nil in dropdowns
        ]
    (if should-show?
      [navbar/navbar-dropdown {:title "Remote"
                               :key "remote"
                               :labels dropdowns}])))

(defn topbar-help-menu
  ""
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        about-el [:a {:href "https://github.com/joshcurtis/Senior-project"
                      :target "_blank"} "Source Code"]
        dropdowns [
                   about-el
                   ]
        should-show? true ; only show if there is a non-nil in dropdowns
        ]
    (if should-show?
      [navbar/navbar-dropdown {:title "Help"
                               :key "help"
                               :labels dropdowns}])))


(defn map-do
  "Not lazy version of map."
  [f coll]
  (doall (map f coll)))

(defn- remote-open-modal-helper
  [dir contents]
  (let [contents (filter topbar-action-filename-filter contents)]
    [:div {:key dir}
     [:h3 dir]
     [:div.list-group
      (map-do #(vector :a.list-group-item {:key %1
                                           :on-click (fn []
                                                       (do
                                                         (close-modal!)
                                                         (remote-manager.controller/edit-file!
                                                          (str "machinekit/configs/" dir %1))))
                                           :style {:cursor "pointer"}}
                       %1)
              contents)]]))


(defn remote-open-modal
  []
  (let [configs @remote-manager.model/configs
        dirs (:dirs configs)
        contents (:contents configs)

        header
        [:h2 "Remote Open"]

        body
        [:div (map-do #(remote-open-modal-helper %1 (get contents %1)) dirs)]

        footer
        [:div
         [:button.btn.btn-default
          {:on-click close-modal!}
          "Cancel"]]]
    (widgets/modal {:header header
                    :body body
                    :footer footer})))

(defn- remote-save-modal-helper
  [dir contents]
  (let [contents (filter topbar-action-filename-filter contents)
        text-input-id (str "rsmhfn-" dir)]
    [:div {:key dir}
     [:h3 dir]
     [:div.list-group
      [:a.list-group-item
       [:div.input-group
        [:input.form-control {:id text-input-id
                              :type "text"}]
        [:span.input-group-btn
         [:button.btn.btn-primary {:on-click #(let [fname (utils/element-value text-input-id)
                                                    path (str "machinekit/configs/" dir fname)]
                                                (remote-manager.controller/upload-file!
                                                 path (topbar-action-save))
                                                (close-modal!))}
          "Upload"]]]]
      (map-do #(vector :a.list-group-item {:key %1
                                           :on-click (fn []
                                                       (let [fname %1
                                                             path (str "machinekit/configs/" dir fname)]
                                                         (close-modal!)
                                                         (remote-manager.controller/upload-file!
                                                          path (topbar-action-save))))
                                           :style {:cursor "pointer"}}
                       %1)
              contents)]]))


(defn remote-save-modal
  []
  (let [configs @remote-manager.model/configs
        dirs (:dirs configs)
        contents (:contents configs)

        header
        [:h2 "Remote Upload"]

        body
        [:div (map-do #(remote-save-modal-helper %1 (get contents %1)) dirs)]

        footer
        [:div
         [:button.btn.btn-default
          {:on-click close-modal!}
          "Cancel"]]]
    (widgets/modal {:header header
                    :body body
                    :footer footer})))

(defn render-topbar
  ""
  []
  (let [app-state @state/app-state
        tab (:tab app-state)]
    [:div
     (case @current-modal
       "remote-open" [remote-open-modal]
       "remote-save" [remote-save-modal]
       nil nil)
     [navbar/navbar {:title tab
                     :elements [(topbar-file-menu)
                                (topbar-remote-menu)
                                (topbar-help-menu)]}]]))
