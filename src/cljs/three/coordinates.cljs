(ns three.coordinates
  ""
  (:require [cljsjs.three]))

(def line-segments (js/eval "THREE.LineSegments"))

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
    (doseq [[x y z] [[start-x start-y start-z] [end-x end-y end-z]]]
      (.push (aget geom "vertices") (js/THREE.Vector3. x y z)))
    ;; (.computeLineDistances geom)
    (let [axis (line-segments. geom mat js/THREE.LinePieces)]
      axis)))
