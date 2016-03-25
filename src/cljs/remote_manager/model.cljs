(ns remote-manager.model
  (:require
   [reagent.core :as r :refer [atom]]))

(defonce connection (atom {:connected? false
                           :connection-pending? false
                           :error nil
                           :hostname ""
                           :username "machinekit"
                           :password "machinekit"}))

(defonce configs (atom {:dirs []
                        :contents {}}))
