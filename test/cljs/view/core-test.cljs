(ns view.core-test
  (:require
   [reagent.core]
   [cljs.test :refer-macros [deftest is are]]))

(deftest react-version
  (is js/React.version "0.14.3"))
