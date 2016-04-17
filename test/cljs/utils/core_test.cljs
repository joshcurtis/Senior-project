(ns utils.core-test
  "Testing for utils/core.cljs"
  (:require
   [cljs.test :refer-macros [deftest is testing run-tests]]
   [utils.core :as utils]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Testing for ids stuff
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest unique-int
  (is (distinct? (map utils/unique-int (range 10000)))))

(deftest gen-unique-id
  (let [ra [:remote "a"]
        rb [:remote "b"]
        la [:local "a"]
        lb [:local "b"]
        ids (set [ra rb la lb])
        new [:remote "c"]]
    (is (= (utils/gen-unique-id ids new) new))
    (is (not= (utils/gen-unique-id ids ra) ra))))

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


(deftest append-line-test
  (is (= "line" (utils/append-line nil "line")))
  (is (= "line\n" (utils/append-line "line" nil)))
  (is (= "line" (utils/append-line "" "line")))
  (is (= "line\n" (utils/append-line "line" "")))
  (is (= "line1\nline2" (utils/append-line "line1" "line2"))))

(deftest remove-idx-test
  (is (= [1 2 3] (utils/remove-idx [0 1 2 3] 0)))
  (is (= [0 1 2] (utils/remove-idx [0 1 2 3] 3)))
  (is (empty? (utils/remove-idx [1] 0))))

(deftest toggle-membership-test
  (is (= #{1} (utils/toggle-membership #{} 1)))
  (is (= #{} (utils/toggle-membership #{1} 1))))

(deftest file-ext-test
  (is (= "" (utils/file-ext "some-file")))
  (is (= "" (utils/file-ext "dir1/dir2/dir3/some-file")))
  (is (= "txt" (utils/file-ext "file.txt")))
  (is (= "txt" (utils/file-ext "dir1/dir2/dir3/file.txt"))))
