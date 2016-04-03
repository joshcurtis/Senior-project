(ns utils.navbar
  ""
  (:require
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]
   [clojure.string :as string]))

(defn navbar-dropdown
  ""
  [props]
  (let [{:keys [title labels]} props]
    (assert (string? title))
    [:li.dropdown {:key title}
     [:a.dropdown-toggle {:data-toggle "dropdown"
                          :role "button"
                          :aria-expanded "false"} title [:span.caret]]
     [:ul.dropdown-menu {:role "menu"}
      (map (fn [l] [:li {:style {:cursor "pointer"}
                         :key l} [:a l]])
           labels)]]))

(defn navbar
  ""
  [props]
  (let [{:keys [title elements]} props
        navbar-id (str "navbar-" (string/lower-case
                                  (string/replace title #" " "-")))
        elements (into '() elements)]
    (assert (string? title))
    (assert (some? elements))
    [:nav.navbar.navbar-default
     [:div.container-fluid
      [:div.navbar-header
       [:button.navbar-toggle.collapsed {:type "button"
                                         :data-toggle "collapse"
                                         :data-target (str \# navbar-id)}
        [:span.sr-only "Toggle Navigation"]
        [:span.icon-bar]
        [:span.icon-bar]
        [:span.icon-bar]]
       [:a.navbar-brand {:style {:cursor "default"}}
        title]]
      [:div.collapse.navbar-collapse {:id navbar-id}
       [:ul.nav.navbar-nav
        elements]]]]))
