(ns server.state
  "The client state and actions associated with it."
  (:require
   [clj-ssh.ssh :as ssh]))

(defonce connection-state (atom {:connected? false
                                 :hostname ""
                                 :username "machinekit"
                                 :agent nil
                                 :session nil}))

(defonce sockets (atom {}))

(def timeout 15000)

(defonce ssh-lock (Object.))

(defn ssh-disconnect!
  []
  (locking  ssh-lock
    (let [{:keys [session]} @connection-state]
      (swap! connection-state assoc
             :connected? false
             :agent nil
             :session nil)
      (if (and (some? session) (ssh/connected? session))
        (ssh/disconnect session)))))

(defn ssh-connect!
  "Disconnects from the current ssh server if needed and the attempts to connect
  given one. An exception is thrown if this fails."
  [hostname username password]
  (ssh-disconnect!)
  (locking ssh-lock
    (let [agent (ssh/ssh-agent {})
          session (let [s (ssh/session agent hostname
                                       {:strict-host-key-checking :no
                                        :username username
                                        :password password})
                        _ (ssh/connect s timeout)]
                    s)]
      (swap! connection-state assoc
             :connected? true
             :hostname hostname
             :username username
             :agent agent
             :session session))))
