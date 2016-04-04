(ns monitor.core
  "Shows some measurements."
  (:require
   [utils.widgets :as widgets]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(def topbar-actions {})

(defn add-tmp [q temp]
  (-> q
      pop
      (conj temp)))

(def tmp (into #queue [] (map #(str %1 "s") (range 15))))
(defonce t (atom (into [] tmp)))
(defonce ext (atom [tmp tmp tmp]))

(defn rand-temp [[exta extb extc]]
  (let [a (rand 1)
        b (rand 2)
        c (rand 3)]
    [(add-tmp exta a)
     (add-tmp extb b)
     (add-tmp extc c)]))

(utils/set-interval "temperature"
                    #(let [a (rand 1)
                           b (rand 1)
                           c (rand 1)]
                       (swap! ext rand-temp))
                    2000)

(defn contents
  "A view that can be rendered to monitor the machinekit configuration. It is
  used in app/core.cljs. This returns a reagent component that takes no props."
  [props]
  (let [t @t
        ext @ext]
    [:div
     [widgets/line-plot {:width 512
                         :height 256
                         :options {:animation false
                                   :bezierCurve true}
                         :data {:labels t
                                :datasets [{:label "EXT01 Temperature"
                                            :fillColor "rgba(255, 0, 0, 0.1)"
                                            :pointHighlightFill "#FF0000"
                                            :data (get ext 0)}
                                           {:label "EXT02 Temperature"
                                            :fillColor "rgba(0, 255, 0, 0.1)"
                                            :pointHighlightFill "#00FF00"
                                            :data (get ext 1)}
                                           {:label "EXT03 Temperature"
                                            :fillColor "rgba(0, 0, 255, 0.1)"
                                            :pointHighlightFill "#0000FF"
                                            :data (get ext 2)}]}}]
   ]))
