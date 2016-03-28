(ns utils.widgets
  ""
  (:require
   [utils.core :as utils]
   [clojure.string :as string]
   [reagent.core :as r :refer [atom]]))

(defn tabs
  "Creates notebook like tabs.
  # Props
  `labels` - sequence of strings for labels
  `selected` - string, the currently selected label
  `on-change` - function that takes a label that was clicked on."
  [props]
  (let [{:keys [labels selected on-change]} props]
    (assert (sequential? labels))
    (assert (string? selected))
    (assert (fn? on-change))
    [:ul.nav.nav-tabs
     (map-indexed (fn [i l] [:li {:style {:cursor "pointer"}
                                  :key i
                                  :class (if (= l selected) "active")
                                  :on-click #(on-change l)}
                             [:a {} l]])
                  labels)]))

(defn file-path
  "Renders a widget which displays a path. Strings will be displayed in a
  slightly faded font, while `a` tags will pop a bit.
  # Props
  `path` - Sequence of strings or [:a ...] which represents the path"
  [props]
  (let [{:keys [path]} props]
    [:ul.breadcrumb
     (map (fn [el] [:li.active {:key el} el])
          path)]))

(defn- pagination-tab
  [element selected-val on-change]
  (let [[label value] (if (vector? element) element [element element])]
    [:li {:class (if (= selected-val value) "active")
          :on-click #(on-change value)
          :key value}
     [:a label]]))

(defn pagination
  "Renders a pagination widget, ie [1 2 3 4]
  # Props
  `labels` - A sequence of [label value] pairs. If a string is provided instead
             of the pair, then it will be both the label and value.
  `selected` - the currently selected value
  `on-change` - called when a label is clicked"
  [props]
  (let [{:keys [labels selected on-change]} props]
    [:ul.pagination.pagination-sm
     (map #(pagination-tab %1 selected on-change)
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
              :value nil
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
    (assert (some? str-func))
    (assert (some? filename))
    (assert (some? element))
    [:span {:on-click #(utils/save-file (str-func) filename)}
     element]))

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
                             [:span.input-group-btn {}
                              [:button.btn.btn-danger
                               {:disabled disabled
                                :on-click #(-> value (utils/remove-idx i) on-change)}
                               "-"]
                              ]])
                  value)
     [:button.btn.btn-primary.btn-block
      {:disabled disabled
       :on-click #(-> value (conj default-value) on-change)} "+"]]))

(defn endlines-to-divs
  "Converts a string into a span that contains a sequence of divs, one for each
  line of the given string."
  [s]
  (assert (string? s))
  [:span (map-indexed (fn [idx s] [:div {:key idx} s])
                      (string/split-lines s))])
