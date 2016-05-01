(ns viz.controls
  "Move the camera around using the mouse in a 3D three scene."
  (:require
   [cljsjs.trackball-controls]
   [cljsjs.three]))

(def tc-constructor (js/eval "THREE.TrackballControls"))

(defn new-controls!
  "Creates controls for the camera. It steals mouse actions such as the mouse
  wheel. Use `stop-controls!` to give back mouse actions."
  [camera]
  (tc-constructor. camera))

(defn update-controls!
  [controls]
  (if (some? controls)
    (.update controls)))

(def dispose!
  (js/eval "var disposeffix=function(o){o.dispose()};disposeffix"))

(defn stop-controls!
  [controls]
  (if (some? controls)
    (dispose! controls))
  nil)
