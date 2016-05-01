(ns app.devtools-setup
  "Bootstraps some devtools. Currently, only pretty formatting is
  supported. Pretty formatting allows better printing of Clojure datastructures
  in the Chrome/Chromium development console."
  (:require
   [devtools.core :as devtools]))

(devtools/install!)
