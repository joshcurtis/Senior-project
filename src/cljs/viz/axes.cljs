(ns viz.axes
  "Use three to render stuff for our project. Very primitive. Very very very
  primitive, lots of stuff is unhandled."
  (:require
   [viz.controls]
   [viz.coordinates]
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
    (aset (.-position floor) "y" -0.01)
    [floor]))

(defn new-coordinate-legend
  [len]
  (let [obj (js/THREE.Object3D.)
        axes (map #(viz.coordinates/build-axis [0 0 0]
                                               (:dir %)
                                               (:color %)
                                               true)
                  [{:dir [len 0 0] :color "red"}
                   {:dir [0 0 (- len)] :color "green"}
                   {:dir [0 len 0] :color "blue"}])]
    (doseq [a axes]
      (.add obj a))
    (.set (aget obj "position") -0.5 0.0 0.75)
    obj))

(def default-camera
  "Defines a default camera, which is to say all the values are 0, except for
  `scalex`, `scaley`, and `scalez` which are set to 1."
  (let [zeros (apply hash-map (mapcat #(vector % 0)
                                      [:x :y :z
                                       :atx :aty :atz
                                       :upx :upy :upz
                                       :rotx :roty :rotz]))
        ones (apply hash-map (mapcat #(vector % 1)
                                     [:scalex :scaley :scalez]))]
    (merge zeros ones)))

(def default-props
  ;; width and height of canvas obj
  {:size {:width 512
          :height 512}
   ;; if mouse controls are allowed to move camera around
   :controls false
   ;; if camera should be updated to props value at each frame,
   ;; this will override controls
   :update-camera false
   ;; check `default-camera` hash-map
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
                upx upy upz
                rotx roty rotz
                scalex scaley scalez]} (merge default-camera props)]
    (.set (aget camera "position") x y z)
    (.set (aget camera "up") upx upy upz)
    (.set (aget camera "scale") scalex scaley scalez)
    (.set (aget camera "rotation") rotx roty rotz)
    (.lookAt camera (js/THREE.Vector3. atx aty atz))))

;; hack needed for release mode
(def renderer-set-clear-color!
  (js/eval "var rscctffix=function(r,c){r.setClearColor(c)};rscctffix"))

(def renderer-set-size!
  (js/eval "var rsstffix=function(r,w,h){r.setSize(w,h)};rsstffix"))

(def dispose!
  (js/eval "var disposeffix=function(o){o.dispose()};disposeffix"))


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
        light (js/THREE.DirectionalLight. "white" 1.5)
        camera (js/THREE.PerspectiveCamera. 75 ;; field of view
                                            (/ width height) ;; aspect ratio
                                            0.001 ;; near clipping
                                            100.0) ;; far clipping
        renderer (js/THREE.WebGLRenderer. #js {:antialias true})
        controls (if controls (viz.controls/new-controls! camera))
        floor-objs (new-floor-objs)
        coordinate-legend (new-coordinate-legend 0.25)
        axis-objs (mapv #(new-axis-obj) (range max-axes))]
    ;; camera & lighting
    (adjust-camera! camera camera-props)
    (.set (aget light "position")
          (:x light-props)
          (:y light-props)
          (:z light-props))
    (.add scene light)
    ;; renderer properties
    (renderer-set-clear-color! renderer background)
    (renderer-set-size! renderer width height)
    ;; set position
    (doseq [i (range max-axes)]
      (axis-set-pos! (get axis-objs i) (get axes i)))
    ;; add stuff to scene
    (.add scene coordinate-legend)
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
                      (aget renderer "domElement"))
        (.render renderer scene camera))

      :component-will-unmount
      (fn []
        (let [context (aget renderer "context")]
          (viz.controls/stop-controls! controls)
          (.removeChild (js/document.getElementById element-id)
                        (aget renderer "domElement"))
          (dispose! renderer)))

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
          (viz.controls/update-controls! controls)
          (.render renderer scene camera)))

      :reagent-render
      (fn [props]
        (let [{:keys [width height]} props]
          [:div {:id element-id}]))})))
