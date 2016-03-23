(ns machine-serve.remote
  "Server side functions that can be called from clojurescript. See
  machine_conf/remote.cljs for the cljs interface."
  (:require
   [clj-ssh.ssh :as ssh]
   [shoreleave.middleware.rpc :refer [defremote]]))

(defn- create-tmp-file
  "TODO: Get rid of the endline. Using the print instead of println function did
  not work in this context."
  [contents]
  (let [file (java.io.File/createTempFile "cljtmpfile" ".tmp")
        writer (clojure.java.io/writer file)]
    (binding [*out* writer] (println contents))
    (.getAbsolutePath file)))

(defn- delete-file
  "Delete a file with the provided filename"
  [filename]
  (clojure.java.io/delete-file filename true))

(defn- sftp-do
  "Run an sftp command with the given args for the given hostname. cmd should be
  a key, ie :put or :get. Currently, the username and password are hard-coded to
  be \"machinekit\""
  [hostname cmd & args]
  (let [agent (ssh/ssh-agent {})
        session (ssh/session agent hostname {:strict-host-key-checking :no
                                             :username "machinekit"
                                             :password "machinekit"})]
    (ssh/with-connection session
      (let [channel (ssh/ssh-sftp session)
            args (conj args cmd {} channel)]
        (ssh/with-channel-connection channel
          (apply ssh/sftp args))))))

(defremote sftp-put
  "Write a string to a remot file by using sftp."
  [hostname contents filename]
  (assert (string? hostname))
  (assert (string? contents))
  (assert (string? filename))
  (let [tmp-file (create-tmp-file contents)]
    (sftp-do hostname :put tmp-file filename)
    (delete-file tmp-file)))

(defremote sftp-get
  "Read remote file contents to a string."
  [hostname filename]
  (assert (string? hostname))
  (assert (string? filename))
  (let [tmp-file (create-tmp-file "")]
    (sftp-do hostname :get filename tmp-file)
    (slurp tmp-file)))
