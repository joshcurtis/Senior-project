(ns remote-manager.core
  "Manage machinekit configuration remotely."
  (:require
   [app.store :as store]
   [remote-manager.view :as view]
   [utils.core :as utils]
   [reagent.core :as r :refer [atom]]))

(defn contents
  "A view that can be rendered to manage the machinekit configuration
  remotely. See machine_conf/core.cljs. This returns a reagent component that
  takes no props."
  [props]
  (let [connection @(r/cursor store/state [:connection])
        configs @(r/cursor store/state [:configs])
        services @(r/cursor store/state [:services])]

    [:div {}
     [view/remote-manager {:connection connection
                           :configs configs
                           :services services}]]))
