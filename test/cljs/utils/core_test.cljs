(ns utils.core-test
  "Testing for utils/core.cljs"
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [utils.core :as utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Testing the parsing of output
;;; resolve.py found in ~/Desktop of
;;; the BB
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def removed-launcher "removed 2 0 Machinekit Launcher on beaglebone.local _machinekit._tcp local 4")
(def removed-launchercmd "resolved 3 0 launchercmd tcp://beaglebone.local:62996")
(def resolved-line "resolved launchercmd tcp://beaglebone.local:62996")


(def resolved-lines ["resolved launchercmd tcp://beaglebone.local:62996",
                    "resolved launcher tcp://beaglebone.local:49764"])

(def services-removed ["resolved launchercmd tcp://beaglebone.local:62996",
                       "resolved launcher tcp://beaglebone.local:49764",
                       "removed 2 0 Machinekit Launcher on beaglebone.local _machinekit._tcp local 4",
                       "removed 3 0 Launchercmd service on beaglebone.local pid 25044 _machinekit._tcp local 4"])

(def empty-log  "")
(def log-with-no-services
  "all for now
  all for now")
(def log-with-services
  "all for now
  all for now
  resolved launchercmd tcp://beaglebone.local:62996
  resolved launcher tcp://beaglebone.local:49764")
(def log-with-stopped-services
  "all for now
  all for now
  resolved launchercmd tcp://beaglebone.local:62996
  resolved launcher tcp://beaglebone.local:49764
  removed 2 0 Machinekit Launcher on beaglebone.local _machinekit._tcp local 4
  removed 3 0 Launchercmd service on beaglebone.local pid 25044 _machinekit._tcp local 4")

(deftest parse-resolved-line-test
  (is (= {:launchercmd "62996"} (utils/parse-resolved-line resolved-line))))

(deftest parse-removed-line-test
  (is (= :launcher (utils/parse-removed-line removed-launcher)))
  (is (= :launchercmd (utils/parse-removed-line removed-launchercmd))))

(deftest parse-service-lines-test
  (is (empty? (utils/parse-service-lines [] {})))
  (is (empty? (utils/parse-service-lines services-removed {})))
  (is (= {:launchercmd "62996", :launcher "49764"} (utils/parse-service-lines resolved-lines {}))))

(deftest clean-service-log-test
  (is (empty? (utils/clean-service-log empty-log)))
  (is (empty? (utils/clean-service-log log-with-no-services)))
  (is (= ["resolved launchercmd tcp://beaglebone.local:62996",
          "resolved launcher tcp://beaglebone.local:49764",
          "removed 2 0 Machinekit Launcher on beaglebone.local _machinekit._tcp local 4",
          "removed 3 0 Launchercmd service on beaglebone.local pid 25044 _machinekit._tcp local 4"]
         (utils/clean-service-log log-with-stopped-services))))

(deftest parse-log-test
  (is (empty? (utils/parse-service-log empty-log)))
  (is (empty? (utils/parse-service-log log-with-no-services)))
  (is (empty? (utils/parse-service-log log-with-stopped-services)))
  (is (= {:launchercmd "62996", :launcher "49764"} (utils/parse-service-log log-with-services))))
