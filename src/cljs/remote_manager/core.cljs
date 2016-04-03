(ns remote-manager.core
  "Manage machinekit configuration remotely."
  (:require
   [remote-manager.model :as model]
   [remote-manager.view :as view]
   [utils.core :as utils]
   [server-interop.core :as server-interop]))

(defn contents
  "A view that can be rendered to manage the machinekit configuration
  remotely. See machine_conf/core.cljs. This returns a reagent component that
  takes no props."
  [props]
  [:div {}
   [view/remote-manager {:connection @model/connection
                         :configs @model/configs}]])
