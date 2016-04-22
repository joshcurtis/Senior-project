(ns three.core
  "Use three to render stuff for our project. Very primitive. Very very very
  primitive, lots of stuff is unhandled."
  (:require
   [reagent.core :as r]
   [three.trackball-controls]
   [cljsjs.three]))

(def id-counter (atom 0))

(defn gen-id []
  (str "r-axes-plot-" (swap! id-counter inc)))

(defn new-colored-mesh
  [color]
  (js/THREE.MeshBasicMaterial. #js {:color color}))

(def red-mesh (new-colored-mesh "red"))
(def green-mesh (new-colored-mesh "green"))
(def blue-mesh (new-colored-mesh "blue"))

(defn aupdate!
  "Uses `aset` and `aget` to update/modify `obj`. `obj` is returned."
  [obj key f]
  {:pre [(string? key) (fn? f)]}
  (let [old (aget obj key)
        new (f old)]
    (aset obj key new))
  obj)

(def axes-width 0.05)
(def axes-height 0.1)

(def far-num 1000000)
(def far-pos {:x far-num :y far-num :z far-num})

(defn axes-set-pos!
  "Moves the axes, if no pos is given, then it will be drawn too far away to
  see.
  Gotchas: If x y or z is nil, then `far-num` is used, so you probably
  won't see it."
  ([axes] (axes-set-pos! axes far-pos))
  ([axes pos]
   {:pre [(or (map? pos) (nil? pos))]}
   (let [pos (or pos far-pos)
         {:keys [point box]} axes
         {:keys [x y z]} pos]
     (-> (.-position point)
         (aupdate! "x" #(+ 0 x))
         (aupdate! "y" #(+ y (/ axes-height 2.0)))
         (aupdate! "z" #(+ 0 z)))
     (-> (.-position box)
         (aupdate! "x" #(+ x 0))
         (aupdate! "y" #(+ y axes-height))
         (aupdate! "z" #(+ z 0)))
     axes)))

(defn new-axes-obj
  ([] (new-axes-obj far-pos))
  ([pos]
   (let [point (js/THREE.Mesh.
                (js/THREE.CylinderGeometry. (/ axes-width 2.0)
                                            0.0
                                            axes-height
                                            16
                                            1
                                            false)
                red-mesh)
         box (js/THREE.Mesh.
              (js/THREE.BoxGeometry. axes-width (/ axes-height 4) axes-width)
              blue-mesh)
         obj {:point point
              :box box}]
     (axes-set-pos! obj pos)
     obj)))

(def default-props
  {:size {:width 512
          :height 512}
   :controls false
   :max-axes 4
   :axes [
          ;; {:x :y :z}
          ]})

(defn axes-plot
  "Gotcha: max-axes can only be defined upon mounting."
  [props]
  (let [{:keys [size controls axes max-axes
                background]} (merge default-props props)
        {:keys [width height]} size
        n-axes (min max-axes (count axes))
        element-id (gen-id)
        scene (js/THREE.Scene.)
        camera (js/THREE.PerspectiveCamera. 75 ;; field of view
                                            (/ width height) ;; aspect ratio
                                            0.001 ;; near clipping
                                            100.0) ;; far clipping
        renderer (js/THREE.WebGLRenderer. #js {:antialias true})
        controls (if controls (js/THREE.TrackballControls. camera))
        axes-objs (mapv #(new-axes-obj) (range max-axes))]
    (.setClearColor renderer background)
    (.setSize renderer width height)
    (aset (.-position camera) "z" 3.0)
    (aset (.-position camera) "y" 2.0)
    (aset (.-position camera) "x" 1.0)
    (.lookAt camera (js/THREE.Vector3. 0.25 1.0 0.0))
    (doseq [i (range max-axes)]
      (axes-set-pos! (get axes-objs i) (get axes i)))
    (doseq [{:keys [point box]} axes-objs]
      (.add scene point)
      (.add scene box))
    (r/create-class
     {:display-name "AxesPlot"

      :component-did-mount
      (fn []
        (.appendChild (js/document.getElementById element-id)
                      (.-domElement renderer))
        (.render renderer scene camera))

      :component-will-unmount
      (fn []
        (let [context (.-context renderer)]
          (.clear renderer)))

      :component-did-update
      (fn [this _]
        (let [props (merge default-props (r/props this))
              {:keys [axes]} props
              n-axes (min max-axes (count axes))]
          (if (not= n-axes (count axes))
            (.warn js/console
                   "Error displaying axes. Probably went over max-axes."))
          (doseq [i (range max-axes)]
            (axes-set-pos! (get axes-objs i) (get axes i)))
          (if (some? controls) (.update controls))
          (.render renderer scene camera)))

      :reagent-render
      (fn [props]
        (let [{:keys [width height]} props]
          [:div {:id element-id}]))})))
