(ns server.csv
  (:require
   [clojure.string :as string]
   [shoreleave.middleware.rpc :refer [defremote]]))

(defremote csv-files
  []
  (-> "./csv"
      clojure.java.io/file
      (remove #(.isDirectory %1))
      (map #(.getName %1))
      (filter #(.endsWith %1 ".csv"))))

(defn- csv-name->fname
  [name]
  (str "./csv/" name ".csv"))

(defn seq->csv-row
  [seq]
  (join ", " seq))

(defremote start-csv-file!
  [name headers]
  (spit (csv-name->fname name)
        (seq->csv-row headers)))

(defremote write-csv-row!
  [name row]
  (spit (csv-name->fname name)
        (seq->csv-row row) :append true))

(defremote write-csv-rows!
  [name rows]
  (spit (csv-name->fname name)
        (join \newline
              (map seq->csv-row rows))
        :append true))
