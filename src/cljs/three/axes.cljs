(ns three.axes
  "Use three to render stuff for our project. Very primitive. Very very very
  primitive, lots of stuff is unhandled."
  (:require
   [three.trackball-controls :as trackball-controls]
   [utils.core :as utils]
   [reagent.core :as r]
   [cljsjs.three]))

(defn aupdate!
  "Uses `aset` and `aget` to update/modify the provided key in `obj`. `obj` is
  modified and returned. The `obj` is returned to make it easier to chain
  actions with the `->`, the `thread` macro."
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

(defn axis-set-pos!
  "Moves the axes, if no pos is given, then it will be drawn too far away to
  see.
  Gotchas: If x y or z is nil, then `far-num` is used, so you probably
  won't see it."
  ([axes] (axis-set-pos! axes far-pos))
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

(defn new-axis-obj
  "Creates an object that can be used to render an axis"
  []
  (let [material (js/THREE.MeshLambertMaterial. #js {:color "#272822"})
        point (js/THREE.Mesh.
               (js/THREE.CylinderGeometry. (/ axes-width 2.0)
                                           0.0
                                           axes-height
                                           16
                                           1
                                           false)
               material)
        box (js/THREE.Mesh.
             (js/THREE.BoxGeometry. axes-width (/ axes-height 4) axes-width)
             material)]
    {:point point
     :box box}))

(defn new-floor-objs
  []
  (let [floor (js/THREE.Mesh.
               (js/THREE.BoxGeometry. 2.5 0.01 2.5)
               (js/THREE.MeshLambertMaterial. #js {:color "#888888"}))]
    [floor]))

(def default-camera
  "Defines a default camera, which is to say all the values are 0. The keys are
  `x`, `y`, `z`, `atx`, `aty`, `atz`, `upx`, `upy`, and `upz`."
  (apply hash-map (map #(vector % 0) [:x :y :z
                                      :atx :aty :atz
                                      :upx :upy :upz])))

(def default-props
  ;; width and height of canvas obj
  {:size {:width 512
          :height 512}
   ;; if mouse controls are allowed to move camera around
   :controls false
   ;; if camera should be updated to props value at each frame,
   ;; this will override controls
   :update-camera false
   ;; hash-map with the keys, `x`, `y`, `z`, `atx`, `aty`, `atz`, `upx`, `upy`,
   ;; and `upz`.
   :camera default-camera
   ;; max amount of axes that will every be shown. This can't be changed after
   ;; instantiation. Exceeding this limit will cause some axes to not be drawn.
   :max-axes 4
   ;; vector of hash-maps with the keys `x`, `y`, and `z`.
   :axes [
          ;; {:x :y :z}
          ]})

(defn adjust-camera!
  "Adjust a camera object based on the `props` map. It can have the same keys as
  `default-camera`."
  [camera props]
  (let [{:keys [x y z
                atx aty atz
                upx upy upz]} (merge default-camera props)]
    (.set (.-position camera) x y z)
    (.set (.-up camera) upx upy upz)
    (.lookAt camera (js/THREE.Vector3. atx aty atz))))

(defn axes-plot
  "Gotcha: max-axes can only be defined upon mounting."
  [props]
  (let [{:keys [size controls axes max-axes
                camera light background]} (merge default-props props)
        {:keys [width height]} size
        camera-props camera
        light-props light
        n-axes (min max-axes (count axes))
        element-id (utils/unique-dom-id "r-axis-plot-")
        scene (js/THREE.Scene.)
        light (js/THREE.PointLight. "white" 2.0)
        camera (js/THREE.PerspectiveCamera. 75 ;; field of view
                                            (/ width height) ;; aspect ratio
                                            0.001 ;; near clipping
                                            100.0) ;; far clipping
        renderer (js/THREE.WebGLRenderer. #js {:antialias true})
        controls (if controls (trackball-controls/new-controls camera))
        floor-objs (new-floor-objs)
        axis-objs (mapv #(new-axis-obj) (range max-axes))]
    ;; camera & lighting
    (adjust-camera! camera camera-props)
    (.set (.-position light)
          (:x light-props)
          (:y light-props)
          (:z light-props))
    (.add scene light)
    ;; renderer properties
    (.setClearColor renderer background)
    (.setSize renderer width height)
    ;; set position
    (doseq [i (range max-axes)]
      (axis-set-pos! (get axis-objs i) (get axes i)))
    ;; add stuff to scene
    (doseq [obj floor-objs]
      (.add scene obj))
    (doseq [{:keys [point box]} axis-objs]
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
          (.clear renderer)
          (trackball-controls/stop-controls controls)
          (.removeChild (js/document.getElementById element-id)
                        (.-domElement renderer))))

      :component-did-update
      (fn [this _]
        (let [props (merge default-props (r/props this))
              {:keys [axes update-camera]} props
              n-axes (min max-axes (count axes))]
          (if (not= n-axes (count axes))
            (.warn js/console
                   "Error displaying axes. Probably went over max-axes."))
          ;; camera & light
          (if update-camera
            (adjust-camera! camera (:camera props)))
          (.set (.-position light)
                (:x (:light props))
                (:y (:light props))
                (:z (:light props)))
          ;;
          (doseq [i (range max-axes)]
            (axis-set-pos! (get axis-objs i) (get axes i)))
          (trackball-controls/update-controls! controls)
          (.render renderer scene camera)))

      :reagent-render
      (fn [props]
        (let [{:keys [width height]} props]
          [:div {:id element-id}]))})))
