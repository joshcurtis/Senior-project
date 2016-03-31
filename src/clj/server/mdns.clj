(ns server.mdns
  (:import (javax.jmdns JmDNS ServiceListener)))

(defn is-ambly-bonjour-name? [& args] false)

(defn setup-mdns
  "Sets up mDNS to populate atom supplied in name-endpoint-map with discoveries.
  Returns a function that will tear down mDNS."
  [reg-type name-endpoint-map]
  {:pre [(string? reg-type)]
   :post [(fn? %)]}
  (let [mdns-service (JmDNS/create)
        service-listener
        (reify ServiceListener
          (serviceAdded [_ service-event]
            (let [type (.getType service-event)
                  name (.getName service-event)]
              (when true
                (.requestServiceInfo mdns-service type name 1))))
          (serviceRemoved [_ service-event]
            (swap! name-endpoint-map dissoc (.getName service-event)))
          (serviceResolved [_ service-event]
            (let [type (.getType service-event)
                  name (.getName service-event)]
              (when true
                (let [entry {name (let [info (.getInfo service-event)]
                                    {:address (.getHostAddress (.getAddress info))
                                     :port    (.getPort info)})}]
                  (swap! name-endpoint-map merge entry))))))]
    (.addServiceListener mdns-service reg-type service-listener)
    (fn []
      (.removeServiceListener mdns-service reg-type service-listener)
      (.close mdns-service))))
