(ns server.state
  "The client state and actions associated with it."
  (:require
   [clj-ssh.ssh :as ssh]))

(defonce connection-state (atom {:connected? false
                                 :hostname ""
                                 :username "machinekit"
                                 :agent nil
                                 :session nil
                                 :sftp-chan nil}))

(def timeout 15000)

(defn ssh-disconnect!
  []
  (let [{:keys [session sftp-chan]} @connection-state]
    (swap! connection-state assoc
           :connected? false
           :agent nil
           :session nil
           :sftp-chan nil)
    (if (and (some? session) (ssh/connected? session))
      (ssh/disconnect session))
    (if (and (some? sftp-chan) (ssh/connected-channel? sftp-chan))
      (ssh/disconnect-channel sftp-chan))))

(defn ssh-connect!
  "Disconnects from the current ssh server if needed and the attempts to connect
  given one. An exception is thrown if this fails."
  [hostname username password]
  (ssh-disconnect!)
  (let [agent (ssh/ssh-agent {})
        session (let [s (ssh/session agent hostname
                                     {:strict-host-key-checking :no
                                      :username username
                                      :password password})
                      _ (ssh/connect s timeout)]
                  s)
        sftp-channel (let [c (ssh/ssh-sftp session)
                           _ (if-not (ssh/connected-channel? c)
                               (ssh/connect-channel c))]
                       c)]
    (swap! connection-state assoc
           :connected? true
           :hostname hostname
           :username username
           :agent agent
           :session session
           :sftp-chan sftp-channel)))
