(ns remote-manager.model
  (:require
   [reagent.core :as r :refer [atom]]))

(defonce connection (atom {:connected? false
                           :connection-pending? false
                           :error nil
                           :hostname ""
                           :username ""
                           :password ""}))

(defonce configs (atom {:dirs []
                        :contents {}}))

(defn remote-configs
  []
  (:dirs @configs))
