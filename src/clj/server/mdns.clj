(ns server.mdns
  (:require
   [clojure.string :as string]
   [clojure.java.shell :refer [sh]]))

(defn discover
  "Returns a vector of hashmaps to strings for the discovered services. Here is
  an example of one of the hashmaps:
  `{:dsn tcp://beaglebone.local:49152,
    :service log,
    :instance d60eff30-f784-11e5-a07c-d03972182598,
    :uuid a42c8c6b-4025-4f83-ba28-dad21114744a}`"
  []
  (let [{:keys [exit out err]} (sh "node" "scripts/discover.js")]
    (read-string (str \[ out \]))))
