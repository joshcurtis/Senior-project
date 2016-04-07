(ns remote-manager.model
  (:require
   [reagent.core :as r :refer [atom]]))

(defonce connection (atom {:connected? false
                           :connection-pending? false
                           :error nil
                           :hostname "beaglebone.local"
                           :username "machinekit"
                           :password ""}))

(defonce configs (atom {:dirs []
                        :contents {}}))

(defonce services (atom {}))
