(ns ini-editor.utils.ini-test
  "Testing for ini_editor/utils.ini.cljs"
  (:require
   [clojure.string :as string]
   [cljs.test :refer-macros [deftest is are]]
   [utils.ini]))

(def good-ini
"
#! {:unimportant true}
[A]
a = 12
b = 321

[B]
c = value
b = other-value
key = value
#! {:type \"multiline\"}
repeated = x
repeated = y
")

(def bad-ini
"
{:forgot \"shebang\"}
[A]
key = value
")

(deftest parse-good-ini
  (is (not (try (utils.ini/parse-ini good-ini)
                nil
                (catch js/Error e (.-message e))))))

(deftest parse-bad-ini
  (is (= (try (utils.ini/parse-ini bad-ini)
              nil
              (catch js/Error e (.-message e)))
         (str "ini utils.ini error: " (-> bad-ini string/split-lines second)))))

;; nil should not be accepted
(deftest parse-nil
  (is (not (string/blank? (try (utils.ini/parse-ini nil)
                               ""
                               (catch js/Error e (.-message e)))))))

(deftest is-ini
  (is (utils.ini/ini? (utils.ini/parse-ini good-ini))))

(deftest ini-correctness
  (let [ini (utils.ini/parse-ini good-ini)]
    (are [sec k v] (= (get-in ini [:values sec k]) v)
      "A" "a" "12"
      "A" "b" "321"
      "B" "c" "value"
      "B" "b" "other-value"
      "B" "key" "value"
      "B" "repeated" ["x" "y"])
    (is (get-in ini [:section-metadata "A" :unimportant] false))
    (is (not (get-in ini [:section-metadata "B" :unimportant] false)))
    (is (= (:section-order ini) '("A" "B")))
    (is (= (get-in ini [:key-order "A"]) '("a" "b")))
    (is (= (get-in ini [:key-order "B"]) '("c" "b" "key" "repeated")))))

(deftest ini-to-str
  (let [ini (utils.ini/parse-ini good-ini)]
    (is (= ini (-> ini utils.ini/ini-to-str utils.ini/parse-ini)))
    (is (= (utils.ini/ini-to-str ini) (-> ini
                                       utils.ini/ini-to-str
                                       utils.ini/parse-ini
                                       utils.ini/ini-to-str)))))
