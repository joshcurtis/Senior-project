(ns ini-editor.parser-test
  "Testing for ini_editor/parser.cljs"
  (:require
   [clojure.string :as string]
   [cljs.test :refer-macros [deftest is are]]
   [ini-editor.parser :as parser]))

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
  (is (not (try (parser/parse-ini good-ini)
                nil
                (catch js/Error e (.-message e))))))

(deftest parse-bad-ini
  (is (= (try (parser/parse-ini bad-ini)
              nil
              (catch js/Error e (.-message e)))
         (str "ini parser error: " (-> bad-ini string/split-lines second)))))

(deftest ini-correctness
  (let [ini (parser/parse-ini good-ini)]
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
  (let [ini (parser/parse-ini good-ini)]
    (is (= ini (-> ini parser/ini-to-str parser/parse-ini)))
    (is (= (parser/ini-to-str ini) (-> ini
                                       parser/ini-to-str
                                       parser/parse-ini
                                       parser/ini-to-str)))))
