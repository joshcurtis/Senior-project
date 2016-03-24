(ns machine-serve.state
  "The client state and actions associated with it."
  (:require
   [clj-ssh.ssh :as ssh]))

(defonce state (atom {:connected? false
                      :hostname nil
                      :username nil
                      :agent nil
                      :session nil
                      :sftp-chan nil}))

(defn ssh-disconnect!
  []
  (let [{:keys [session sftp-chan]} @state]
    (swap! state assoc
           :connected? false
           :hostname nil
           :username nil
           :agent nil
           :session nil
           :sftp-chan nil)
    (if (ssh/connected? session) (ssh/disconnect session))
    (if (ssh/connected-channel? sftp-chan) (ssh/disconnect-channel sftp-chan))))

(defn ssh-connect!
  "Disconnects from the current ssh server if needed and the attempts to connect
  given one. An exception is thrown if this fails."
  [hostname username password]
  (let [agent (ssh/ssh-agent {})
        session (let [s (ssh/session agent hostname
                                     {:strict-host-key-checking :no
                                      :username username
                                      :password password})
                      _ (ssh/connect s)]
                  s)
        sftp-channel (let [c (ssh/ssh-sftp session)
                           _ (if-not (ssh/connected-channel? c)
                               (ssh/connect-channel c))]
                       c)]
    (swap! state assoc
           :connected? true
           :hostname hostname
           :username username
           :agent agent
           :session session
           :sftp-chan sftp-channel)))
