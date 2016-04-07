(ns server.client-interop
  "Server side functions that can be called from clojurescript. See
  cljs/server_interop/core.cljs for the cljs interface."
  (:require
   [server.state :as state]
   [clojure.string :as string]
   [clj-ssh.ssh :as ssh]
   [zeromq.zmq :as zmq]
   [shoreleave.middleware.rpc :refer [defremote]])
  (:import
   [org.apache.commons.codec.binary Hex]))

(defn- create-tmp-file
  "TODO: Get rid of the endline. Using the print instead of println function did
  not work in this context."
  [contents]
  (let [file (java.io.File/createTempFile "cljtmpfile" ".tmp")
        fname (.getAbsolutePath file)]
    (spit fname contents)
    fname))

(defn- delete-file
  "Delete a file with the provided filename"
  [filename]
  (clojure.java.io/delete-file filename true))

(defn- ls-filter-pred
  "Filters out the \".\" and \"..\" directories."
  [entry]
  (let [name (.getFilename entry)]
    (and (not= name ".") (not= name ".."))))

(defn- format-ls
  "Used in sftp-ls to fix the return value. l is a Java vector of Class
  ChannelSftp.LsEntry, which is from the Jsch Java api."
  [ls]
  (let [ls (filter ls-filter-pred ls)]
    (mapv (fn [f] (let [name (.getFilename f)
                        attr (.getAttrs f)
                        dir? (.isDir attr)]
                    (if dir? (str name \/) name)))
          ls)))

(defremote ssh-disconnect!
  []
  (state/ssh-disconnect!)
  nil)

(defremote ssh-connect!
  "Connect remotely through ssh. Returns true if successful, otherwise false."
  [hostname username password]
  (try (state/ssh-connect! hostname username password)
       nil
       (catch Exception e (.getMessage e))))

(defremote sftp-put
  "Puts a file through sftp. It is required that the server is connected
  through ssh."
  [contents filename]
  (assert (string? contents))
  (assert (string? filename))
  (locking state/ssh-lock
    (let [tmp-file (create-tmp-file contents)]
      (ssh/sftp (:sftp-chan @state/connection-state) {} :put tmp-file filename)
      (delete-file tmp-file))))

(defremote sftp-get
  "Gets the contents of a file through sftp. It is required that the server is
  connected through ssh."
  [filename]
  (assert (string? filename))
  (locking state/ssh-lock
    (let [tmp-file (create-tmp-file "")]
      (ssh/sftp (:sftp-chan @state/connection-state) {} :get filename tmp-file)
      (let [s (slurp tmp-file)]
        (delete-file tmp-file)
        s))))

(defremote sftp-ls
  "Runs the ls command over the remote ssh server. Directories will end with a /
  character.
  Note: Never pass the empty string to ssh/sftp"
  [path]
  (locking state/ssh-lock
    (format-ls (ssh/sftp (:sftp-chan @state/connection-state)
                         {}
                         :ls (if (string/blank? path) "./" path)))))

(defremote sftp-rm
  "Runs the rm command over the remote ssh server."
  [path]
  (locking state/ssh-lock
    (ssh/sftp (:sftp-chan @state/connection-state)
              {}
              :rm path)))

(defremote connection-status
  "Get the connection status of the server. This involves things such as being
  connected, username, hostname, and etc... See source code for more details."
  []
  (select-keys @state/connection-state [:connected?
                                        :hostname
                                        :username]))
(defremote run-ssh-command
  "Runs the cmd passed as a string.
   It returns the exit code, stdout, and stderr"
  [cmd]
  (locking state/ssh-lock
    (ssh/ssh (:session @state/connection-state) {:cmd cmd})))


(def config-port 51419)
(def command-port 64549)

(defremote send-data
  "Socket testing functionality"
  [data]
  (let [endpoint (str "tcp://" (:hostname @state/connection-state) ":" command-port)
        context (zmq/zcontext)
        identity (.getBytes "machinekit-client")
        hexstr (string/join (map #(format "%02X" %) data))
        buffer (Hex/decodeHex (.toCharArray hexstr))
        ]
    (with-open [dealer (doto  (zmq/socket context :dealer)
                         (zmq/connect endpoint))]
      (zmq/set-identity dealer identity)
      (zmq/send dealer buffer)
      data)))
