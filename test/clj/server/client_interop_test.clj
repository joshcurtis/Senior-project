(ns server.client-interop-test
  "Testing for server/client_interop.clj"
  (:require
   [clojure.test :refer [deftest is testing run-tests]]
   [server.client-interop :as client-interop]
   [server.state :as state]))

(deftest socket-test
  (client-interop/add-socket! "localhost" :launcher 23458 :dealer)
  (is (= 1 (count @state/sockets)))
  (client-interop/remove-socket! :launcher)
  (is (= 0 (count @state/sockets)))
  (reset! state/sockets {}))
