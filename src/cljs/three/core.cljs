(ns three.core
  (:require
   [reagent.core :as r]
   [cljsjs.three]))

(def default-cam {:x 0
                  :y 0
                  :z 0})

(def id-counter (atom 0))

(defn gen-id []
  (str "r-axes-plot-" (swap! id-counter inc)))

(defn create-cone
  []
  (js/THREE.Mesh.
   (js/THREE.CylinderGeometry. 0 1 1 4 1 true)
   (js/THREE.MeshBasicMaterial. #js {:color "red"})))

;; todo, update when size changes
(defn axes-plot
  [props]
  (let [{:keys [size]} props
        element-id (gen-id)
        scene (js/THREE.Scene.)
        camera (js/THREE.PerspectiveCamera. 75 1 0.1 1000)
        renderer (js/THREE.WebGLRenderer.)
        cone (create-cone)]
    (.setSize renderer (:width size) (:height size))
    (aset (.-position camera) "z" 5)
    (.add scene cone)
    (r/create-class
     {:display-name "AxesPlot"

      :component-did-mount
      (fn []
        (.appendChild (js/document.getElementById element-id)
                      (.-domElement renderer))
        (.render renderer scene camera))

      :component-will-update
      (fn []
        (.render renderer scene camera))

      :reagent-render
      (fn [props]
        (let [{:keys [width height]} props]
          [:div {:id element-id}]))})))
