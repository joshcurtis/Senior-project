(ns utils.core-test
  "Testing for utils/core.cljs"
  (:require
    [cljs.test :refer-macros [deftest is testing run-tests]]))

(deftest simple
  (is (= 1 1)))

(run-tests)
