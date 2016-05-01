(ns widgets.rd3
  "React d3 adapter for reagent."
  (:require
   [reagent.core :as r]
   [cljsjs.d3]
   [cljsjs.rd3]))

(def line-chart (r/adapt-react-class js/rd3.LineChart))
