(ns three.coordinates
  ""
  (:require [cljsjs.three]))

(defn build-axis
  [[start-x start-y start-z] [end-x end-y end-z] color dashed?]
  (let [geom (js/THREE.Geometry.)
        mat (if dashed?
              (js/THREE.LineDashedMaterial. #js {:linewidth 3
                                                 :color color
                                                 :dashSize 3
                                                 :gapSize 3})
              (js/THREE.LineBasicMaterial. #js {:linewidth 3
                                                :color color}))]
    (.push (.-vertices geom) (js/THREE.Vector3. start-x start-y start-z))
    (.push (.-vertices geom) (js/THREE.Vector3. end-x end-y end-z))
    (.computeLineDistances geom)
    (let [axis (js/THREE.LineSegments. geom mat js/THREE.LinePieces)]
      axis)))
