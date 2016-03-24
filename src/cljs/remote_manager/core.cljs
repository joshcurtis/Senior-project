(ns remote-manager.core
  "Manage machinekit configuration remotely."
  (:require
   [remote-manager.model :as model]
   [remote-manager.view :as view]
   [machine-conf.utils :as utils]
   [machine-conf.server-interop :as server-interop]))

(defn view
  "A view that can be rendered to manage the machinekit configuration
  remotely. See machine_conf/core.cljs. This returns a reagent component that
  takes no props."
  [props]
  [:div {}
   [view/remote-manager {:connection @model/connection}]])
