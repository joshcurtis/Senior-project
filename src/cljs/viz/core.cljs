(ns viz.core
  "Visualize things in 3D. Used for showing axes. Uses three js graphics API
  which uses WebGL."
  (:require
   [viz.axes :as axes]))

(def axes-plot axes/axes-plot)
