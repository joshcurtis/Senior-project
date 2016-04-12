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

(defn add-socket!
  "type should be one of :dealer or :subscriber
  TODO: Possibly choose the type based on the service"
  [service port type]
  (let [endpoint (str "tcp://" (:hostname @state/connection-state) ":" port)
        context (zmq/zcontext)
        identity (.getBytes "machinekit-client")
        socket (doto  (zmq/socket context :dealer)
                 (zmq/connect endpoint))]
    (zmq/set-identity socket identity)
    (swap! state/sockets assoc service socket)))

(defn remove-socket!
  [service]
  (zmq/close (service @state/sockets))
  (swap! state/sockets dissoc service))

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
  {:pre [(string? filename)]}
  (try
    (locking state/ssh-lock
      (let [tmp-file (create-tmp-file "")]
        (ssh/sftp (:sftp-chan @state/connection-state) {} :get filename tmp-file)
        (let [s (slurp tmp-file)]
          (delete-file tmp-file)
          {:out s :error nil})))
    (catch Exception e {:out nil :error (.getMessage e)})))

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

(defn format-data
  [data]
  (let [hexstr (string/join (map #(format "%02X" %) data))]
    (Hex/decodeHex (.toCharArray hexstr))))

(defremote send-data
  "Socket testing functionality"
  [service data]
  (let [buffer (format-data data)
        socket (service @state/sockets)]
    (zmq/send socket buffer) data))
