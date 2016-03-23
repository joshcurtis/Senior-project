(ns machine-conf.widgets
  ""
  (:require
   [machine-conf.utils :as utils]
   [reagent.core :as r :refer [atom]]))

(defn tabs
  "Creates notebook like tabs.
  # Props
  `labels` - sequence of strings for labels.
  `selected` - string, the currently selected label
  `on-change` - function that takes a label that was clicked on."
  [props]
  (let [{:keys [labels selected on-change]} props]
    (assert (sequential? labels))
    (assert (string? selected))
    (assert (fn? on-change))
    [:ul.nav.nav-tabs
     (map-indexed (fn [i l] [:li {:key i
                                  :class (if (= l selected) "active")
                                  :on-click #(on-change l)}
                             [:a {} l]])
                  labels)]))

(defn dropdown-input
  "Renders a dropdown with options.
  # Props
  `disabled` - `true` if the component should be disabled.
  `options` - A `sequence` of `string` or `[label-string, value-string]`.
  `value` - The current value of the component.
  `on-change` - A function that accepts a `string`. It is called when an option
  is clicked."
  [props]
  (let [{:keys [disabled options value on-change]} props]
    [:select.form-control {:disabled disabled
                           :on-change #(-> %1 .-target .-value on-change)
                           :value value}
     (map (fn [o] (let [[l v] (if (vector? o) o [o o])]
                    [:option {:key v
                              :value v} l]))
          options)]))

(defn- from-file-list
  "Given a JavaScript FileList, a `list` of JavaScript File objects is
  returned.
  file-list - JavaScript FileList"
  [file-list]
  (let [len (.-length file-list)]
    (map #(.item file-list %1) (range len))))

(defn file-input
  "Renders an element that can be used to query a file from the user.
  # Props
  `id` - A unique id string. Failure to do so will cause the element to become
  un-clickable.
  `file-types` - Specifies the accepted file types. Use an empty string to
  accept all types.
  `on-change` - A function that accepts a `list of JavaScript File`.
  `element` - This component will look exactly like `element`. If no `element`
  is provided, then a button with the text 'Upload File' will be displayed."
  [props]
  (let [{:keys [id file-types on-change element]} props]
    (assert (some? id) "No id was provided")
    (assert (some? on-change) "No on-change was provided.")
 [:span {:on-click #(utils/click-element id)}
     (if (some? element) element
         [:button.btn.btn-default.btn-xs {} "Upload File"])
     [:input {:id id
              :style {:height "0px"
                      :width "0px"
                      :overflow "hidden"}
              :on-change #(-> %1
                              .-target
                              .-files
                              from-file-list
                              on-change)
              :type "file"
              :accept file-types}]]))

(defn file-save
  "Renders an element that can be used to save a file to disk.
  # Props
  `str-func` - Function that returns a string that will be downloaded.
  `filename` - target filename to be downloaded
  `element` - This component will look exactly like `element`. Downloading will
  happen when it is clicked."
  [props]
  (let [{:keys [str-func element filename]} props]
    (assert (and (some? str-func) (some? filename) (some? element))
            "Missing props")
    [:span {:on-click #(utils/save-file (str-func) filename)} element]))


(defn list-input
  "Renders an element that can be used to edit a vector of values.
  # Props
  :disabled - bool
  :on-change - function that takes vector of new values
  :value - vector of current value
  :default-value - The value used when a new item is added."
  [props]
  (let [{:keys [disabled on-change value default-value]} props]
    (assert (vector? value) "value passed to list-input must be a vector.")
    [:span {}
     (map-indexed (fn [i v] [:div.input-group {:key i}
                             [:input.form-control
                              {:disabled disabled
                               :on-change #(->>
                                            %1
                                            .-target
                                            .-value
                                            (assoc value i)
                                            on-change)
                               :value v}]
                             [:span.input-group-btn {} [:button.btn.btn-danger
                                                        {:on-click
                                                         #(->
                                                           value
                                                           (utils/remove-idx i)
                                                           on-change)}
                                                        "-"]
                              ]])
                  value)
     [:button.btn.btn-primary.btn-block
      {:on-click #(-> value (conj default-value) on-change)} "+"]]))
