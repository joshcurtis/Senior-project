(ns app.topbar
  (:require
   [app.store :as store]
   [remote-manager.controller]
   [ini-editor.core]
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
  (get topbar-actions-map (:tab @store/state)))

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

(defn- topbar-file-open-callback
  [file-list]
  (let [file (first file-list)
        f-id [:local (.-name file)]]
    (if (some? file)
      (utils/read-file file #(topbar-action-open f-id %1)))))

(defn topbar-file-menu
  ""
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        pointer-style {:cursor "pointer"}
        open-el  [:li {:key "open"
                       :style pointer-style
                       :on-click #(utils/click-element "file-input")}
                   [:a
                     [widgets/file-input {:id "file-input"
                                          :file-types nil
                                          :element "Open"
                                          :on-change topbar-file-open-callback}] "Open File"]]
        save-el  [:li {:key "download"
                       :style pointer-style
                       :on-click #(utils/save-file (topbar-action-save) (topbar-action-filename))}
                   [:a "Download"]]
        close-el [:li {:key "close"
                       :style pointer-style
                       :on-click topbar-action-close}
                   [:a "Close"]]
        dropdowns [(if (contains? valid-options "save") open-el)
                   (if (and (contains? valid-options "save")
                            (contains? valid-options "filename")) save-el)
                   (if (contains? valid-options "close") close-el)]]
    ; only show if there is a non-nil in dropdowns
    (if (some some? dropdowns)
      [navbar/navbar-dropdown {:title "File"
                                 :list-items dropdowns}])))

(defn topbar-remote-menu
  "Display a dropdown to open and save remote files."
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        pointer-style {:cursor "pointer"}
        open-el   [:li {:key "open"
                        :style pointer-style
                        :on-click #(reset! current-modal "remote-open")}
                    [:a "Open"]]
        upload-el [:li {:key "upload"
                        :style pointer-style
                        :on-click #(reset! current-modal "remote-save")}
                    [:a "Upload"]]
        dropdowns [(if (contains? valid-options "open") open-el)
                   (if (and (contains? valid-options "save")
                            (contains? valid-options "filename")) upload-el)]]
    ; only show if there is a non-nil in dropdowns
    (if (some some? dropdowns)
      [navbar/navbar-dropdown {:title "Remote"
                                 :list-items dropdowns}])))

(defn topbar-help-menu
  "Display a dropdown for help options."
  []
  (let [valid-options (into #{} (keys (current-topbar-actions)))
        pointer-style {:cursor "pointer"}
        about-el [:li {:key "about"
                       :style pointer-style
                       :on-click #(reset! current-modal "about-modal")}
                   [:a "About"]]]
    [navbar/navbar-dropdown {:title "Help"
                               :list-items [about-el]}]))


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
                                                          dir %1)))
                                           :style {:cursor "pointer"}}
                       %1)
              contents)]]))


(defn remote-open-modal
  []
  (let [configs @(r/cursor store/state [:configs])
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
                                                    config dir]
                                                (remote-manager.controller/upload-file!
                                                 config fname (topbar-action-save))
                                                (close-modal!))}
          "Upload"]]]]
      (map-do #(vector :a.list-group-item {:key %1
                                           :on-click (fn []
                                                       (let [fname %1
                                                             config dir]
                                                         (remote-manager.controller/upload-file!
                                                          config fname (topbar-action-save))
                                                         (close-modal!)))
                                           :style {:cursor "pointer"}}
                       %1)
              contents)]]))


(defn remote-save-modal
  []
  (let [configs @(r/cursor store/state [:configs])
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

(defn about-modal
  []
  (let []
    [widgets/modal {:header [:h2 "MachineKit Manager"]
                    :body [:div
                           [:p "Manage your MachineKit configuration in a sane manner."]
                           [:p "Maintainers: Josh Curtis, Shub Gogna, & Will Medrano"]]
                    :footer [:div [:button.btn.btn-default
                                   {:on-click close-modal!}
                                   "OK"]]}]))

(defn topbar-connection-status
  []
  (let [connected? @(r/cursor store/state [:connection :connected?])]
    [:li {:key "connection-status"}
     [:a
      [:span.label {:class (if connected? "label-info" "label-warning")}
       (if connected? "Connected" "Disconnected")]]]))

(defn render-topbar
  ""
  []
  (let [tab @(r/cursor store/state [:tab])]
    [:div
     (case @current-modal
       "remote-open" [remote-open-modal]
       "remote-save" [remote-save-modal]
       "about-modal" [about-modal]
       nil nil)
     [navbar/navbar {:title tab
                     :elements [(topbar-file-menu)
                                (topbar-remote-menu)
                                (topbar-help-menu)]
                     :right-elements [(topbar-connection-status)]}]]))
