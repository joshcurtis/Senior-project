(ns view.core
  (:require
   [view.ini-editor]
   [view.monitor]
   [view.remote]
   [view.text-editor]))

(def remote view.remote/contents)
(def monitor view.monitor/contents)
(def ini view.ini-editor/contents)
(def text view.text-editor/contents)
