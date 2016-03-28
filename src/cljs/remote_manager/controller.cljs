(ns remote-manager.controller
  (:require
   [utils.core :as utils]
   [remote-manager.model :as model]
   [ini-editor.controller]
   [server-interop.core :as server-interop]
   [clojure.string :as string]))

(defn set-hostname!
  [name]
  (swap! model/connection assoc :hostname name))

(defn set-username!
  [name]
  (swap! model/connection assoc :username name))

(defn set-password!
  [password]
  (swap! model/connection assoc :password password))

(defn clear-configs!
  []
  (reset! model/configs {:dirs [] :contents {}}))

(defn update-config!
  "Updates a single config based on the server. There isn't really a reason to
  call this function. Prefer to use `update-configs!`, which uses this function."
  [dir]
  (assert (utils/dir? dir))
  (let [callback (fn [files]
                   (swap! model/configs assoc-in [:contents dir] files))]
    (server-interop/sftp-ls (str "machinekit/configs/" dir) callback)))

(defn update-configs!
  "Updates the configs based on the server."
  []
  (let [callback (fn [dirs]
                   (swap! model/configs assoc :dirs (filterv utils/dir? dirs))
                   (doseq [dir dirs] (update-config! dir)))]
    (clear-configs!)
    (server-interop/sftp-ls "machinekit/configs/" callback)))

(utils/set-interval "update-configs-when-empty"
                    #(if (and (:connected? @model/connection)
                              (empty? (:dirs @model/configs)))
                       (update-configs!))
                    2000)

(defn connect!
  []
  (let [{:keys [hostname username password]} @model/connection
        callback (fn [res] (if (nil? res) (do
                                            (swap! model/connection assoc
                                                   :connected? true
                                                   :connection-pending? false
                                                   :error nil)
                                            (update-configs!))
                             (swap! model/connection assoc
                                    :connected? false
                                    :connection-pending? false
                                    :error res)))]
    (swap! model/connection assoc
           :connection-pending? true
           :error nil)
    (server-interop/ssh-connect! hostname username password callback)))

(defn disconnect!
  []
  (server-interop/ssh-disconnect!)
  (swap! model/connection assoc
         :connected? false
         :connection-pending? false
         :error nil))

(defn- edit-ini!
  [s id]
  (assert (string? s))
  (assert (some? id))
  (ini-editor.controller/load-str! id s))

(defn- edit-unsupported
  [s id]
  (js/alert "Unsupported file type"))

(def
  edit-callbacks {"ini" edit-ini!})

(defn edit-file!
  [full-filename]
  (assert (string? full-filename))
  (assert (string/includes? full-filename "machinekit"))
  (let [extension (utils/file-ext full-filename)
        callback (get edit-callbacks extension edit-unsupported)
        callback #(callback %1 [:remote full-filename])]
    (server-interop/sftp-get full-filename callback)))

(defn save-file!
  [full-filename]
  (assert (string? full-filename))
  (assert (string/includes? full-filename "machinekit"))
  (let [fname (utils/fname-from-path full-filename)]
    (server-interop/sftp-get full-filename
                             #(utils/save-file %1 fname))))


(defn- update-c-status-helper!
  [status]
  (let [{:keys [connected? hostname username]} status]
    (if connected?
      (swap! model/connection assoc
             :connected? connected?
             :hostname hostname
             :username username)
      (swap! model/connection assoc
             :connected? connected?))))

(defn update-connection-status!
  []
  (server-interop/connection-status update-c-status-helper!))

(utils/set-interval "update-connection-status!"
                    update-connection-status!
                    2000)
