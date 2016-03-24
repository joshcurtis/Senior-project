(ns machine-serve.client-interop
  "Server side functions that can be called from clojurescript. See
  machine_conf/server-interopt.cljs for the cljs interface."
  (:require
   [machine-serve.state :as state]
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
  (let [tmp-file (create-tmp-file contents)]
    (ssh/sftp (:sftp-chan @state/state) {} :put tmp-file filename)
    (delete-file tmp-file)))

(defremote sftp-get
  "Gets the contents of a file through sftp. It is required that the server is
  connected through ssh."
  [filename]
  (assert (string? filename))
  (let [tmp-file (create-tmp-file "")]
    (ssh/sftp (:sftp-chan @state/state) {} :get filename tmp-file)
    (slurp tmp-file)))

(defn- format-ls
  "Used in sftp-ls to fix the return value. l is a java vector of class
  ChannelSftp.LsEntry"
  [ls]
  (mapv (fn [f] (let [name (.getFilename f)
                      attr (.getAttrs f)
                      tp (if (.isDir attr) :dir :file)]
                  {:name name :type tp}))
        ls)

(defremote sftp-ls
  "Runs the ls command over the remote ssh server."
  [path]
  (format-ls (ssh/sftp (:sftp-chan @state/state) {} :ls path)))

(defremote status
  "Get the status of the server. This involves things such as being connected,
  username, hostname, and etc..."
  []
  (select-keys @state/state [:connected?
                             :hostname
                             :username]))
