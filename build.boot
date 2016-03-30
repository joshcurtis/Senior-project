(set-env!
 :source-paths #{"src/cljs" "src/clj"}
 :resource-paths #{"src/html"}
 :target-path #{"target"}

 :dependencies '[[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [adzerk/boot-cljs "1.7.228-1"]
                 [adzerk/boot-cljs-repl "0.3.0"]
                 [adzerk/boot-reload "0.4.5"]
                 [pandeiro/boot-http "0.7.3"]
                 [com.cemerick/piggieback "0.2.1"]     ;; needed by bREPL
                 [weasel "0.7.0"]                      ;; needed by bREPL
                 [org.clojure/tools.nrepl "0.2.12"]
                 [codox "0.9.4"]
                 [hiccup "1.0.5"]
                 [compojure "1.5.0"]
                 [org.clojars.magomimmo/shoreleave-remote-ring "0.3.1"]
                 [org.clojars.magomimmo/shoreleave-remote "0.3.1"]
                 [javax.servlet/servlet-api "2.5"]
                 [clj-ssh "0.5.14"]
                 [org.zeromq/cljzmq "0.1.4"]
                 [reagent "0.6.0-alpha"]
                 ]
 )

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[adzerk.boot-reload :refer [reload]]
         '[pandeiro.boot-http :refer [serve]]
         '[codox.main :refer [generate-docs]])

(deftask dev-options []
  (task-options! cljs {:optimizations :none :source-map true}
                 reload {:on-jsload 'app.core/start})
  identity)

(deftask dev
  "Launch Immediate Feedback Development Environment"
  []
  (comp
   (dev-options)
   (serve :handler 'server.core/handler
          :resource-root "target"
          :reload true)
   (watch)
   (reload)
   (cljs-repl) ;; before cljs task
   (cljs)
   (target :dir #{"target"})))

(deftask doc
  "Generate HTML Code Documentation"
  []
  (do
   (generate-docs {:language :clojurescript
                   :output-path "target/cljs-doc"})
   (generate-docs {:language :clojure
                   :output-path "target/clj-doc"})))
