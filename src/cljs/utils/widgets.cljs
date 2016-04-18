(ns utils.widgets
  ""
  (:require
   [utils.core :as utils]
   [clojure.string :as string]
   [clojure.data]
   [reagent.core :as r :refer [atom]]))

(defn tabs
  "Creates notebook like tabs.
  # Props
  `labels` - sequence of strings for labels
  `id-prefix` - each tab will have the id {id-prefix}{label}
  `selected` - string, the currently selected label
  `on-change` - function that takes a label that was clicked on."
  [{:keys [labels selected on-change id-prefix]}]
  {:pre [(sequential? labels) (string? selected) (fn? on-change)]}
  (let [id-prefix (or id-prefix "")]
    [:ul.nav.nav-tabs
     (map-indexed (fn [i l] [:li {:style {:cursor "pointer"}
                                  :key i
                                  :id (str id-prefix l)
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
          :style {:cursor (if (= selected-val value) "default" "pointer")}
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
  "Returns a tiny/invisible file input component.
  # Props
  `id` - A unique id string. Failure to do so will cause the element to become
  un-clickable.
  `file-types` - Specifies the accepted file types. Use an empty string to
  accept all types.
  `on-change` - A function that accepts a `list of JavaScript File`."
  [props]
  (let [{:keys [id file-types on-change]} props]
    (assert (some? id) "No id was provided")
    (assert (some? on-change) "No on-change was provided.")
    [:input {:id id
             :type "file"
             :accept file-types
             :style {:height "0px" :width "0px"}
             :value nil
             :on-change #(-> %1
                              .-target
                              .-files
                              from-file-list
                              on-change)}]))

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
                                :on-click #(-> value
                                               (utils/remove-idx i)
                                               on-change)}
                               "-"]
                              ]])
                  value)
     [:button.btn.btn-primary.btn-block
      {:disabled disabled
       :on-click #(-> value (conj default-value) on-change)} "+"]]))

(defn modal
  "Renders a modal."
  [{:keys [header body footer]}]
  [:div.modal {:style {:display "initial"
                       :overflow "scroll"}}
   [:div.modal-dialog
    [:div.modal-content
     [:div.modal-header header]
     [:div.modal-body body]
     [:div.modal-footer footer]]]])

(def plotly
  "Renders a line line plot Plotly Documentation -
  https://plot.ly/javascript/line-charts/#basic-line-plot Note: Changing layout
  is not supported. Will have to delete and recreate component to update layout.
  # Props
  `data` - see Plotly documentation
  `layout` - see Plotly documentation
  ... - All other props are passed to the div"
  (r/create-class
   {
    :get-initial-state (fn [this]
                         (let [props (r/props this)
                               id (or (:id props)
                                      (str "line-plot-" (utils/unique-int)))]
                           {:id id
                            :plot nil}))
    :component-did-mount (fn [this]
                           (let [{:keys [id]} (r/state this)
                                 {:keys [data layout]} (r/props this)
                                 plot (.plot js/Plotly
                                             id
                                             (clj->js data)
                                             (clj->js layout))]
                             (r/set-state this {:plot plot})))
    :component-did-update (fn [this _]
                            (let [state (r/state this)
                                  {:keys [id plot]} state
                                  el (.getElementById js/document id)
                                  old-data (:data state)
                                  old-layout (:layout state)
                                  {:keys [data layout]} (r/props this)]
                              (aset el "data" (clj->js data))
                              (aset el "layout" (clj->js layout))
                              (.redraw js/Plotly el)))
    :render (fn [this]
              (let [{:keys [id]} (r/state this)
                    div-props (assoc (r/props this) :id id)]
                [:div.r-plotly div-props]))
    }))

(defn infosection
  "Renders information such as the available files for editing and their source/path.
  # Props
  `selected-id` - [source filepath] or nil
  `all-ids` - seq of [source filepath] or nil
  `on-change-id` - fn [[source filepath]]"
  [{:keys [selected-id all-ids on-change-id]}]
  {:pre [(or (vector? selected-id) (nil? selected-id))
         (or (sequential? all-ids) (nil? all-ids))
         (fn? on-change-id)]}
  [:div
   (if (pos? (count all-ids))
     [pagination
      {:labels (map (fn [[source fname]]
                      [(utils/fname-from-path fname) [source fname]])
                    all-ids)
       :selected selected-id
       :on-change on-change-id}])
   (if (some? selected-id) [file-path {:path (concat
                                              [[:a (str (first selected-id))]]
                                              (string/split (second selected-id)
                                                            \/))}])])

(defn endlines-to-divs
  "Converts a string into a span that contains a sequence of divs, one for each
  line of the given string."
  [s]
  (assert (string? s))
  [:span (map-indexed (fn [idx s] [:div {:key idx} s])
                      (string/split-lines s))])
