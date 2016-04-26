(defproject machineview "0.1.0-SNAPSHOT"
  :description "A clojurescript application for monitoring machinekit running on a beaglebone."
  :url "http://joshcurtis.github.io/MachineView/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.0"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [codox "0.9.4"]
                 [binaryage/devtools "0.6.0"]
                 [hiccup "1.0.5"]
                 [compojure "1.5.0"]
                 [cljs-http "0.1.40"]
                 [javax.servlet/servlet-api "2.5"]
                 [reagent "0.6.0-alpha"]
                 [figwheel-sidecar "0.5.2"]
                 [com.cemerick/piggieback "0.2.1"]
                 [cljsjs/d3 "3.5.16-0"]
                 [cljsjs/three "0.0.72-0"]]

  :plugins [[lein-figwheel "0.5.2"]
            [lein-doo "0.1.6"]
            [lein-codox "0.9.4"]
            [lein-cljsbuild "1.1.3" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src/clj" "src/cljs" "test/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :profiles {:clj {:codox {:soure-paths ["src/clj"]
                           :output-path "clj_doc"}}
             :cljs {:codox {:language :clojurescript
                            :source-paths ["src/cljs"]
                            :output-path "cljs_doc"}}}

  :doo {:build "test"}

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src"]

                ;; If no code is to be run, set :figwheel true for continued automagical reloading
                :figwheel {:on-jsload "app.core/start"}

                :compiler {:main app.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/core.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}

               {:id "test"
                :source-paths ["src" "test"]

                :compiler {:main app.runner
                           :output-to "resources/public/js/compiled/test.js"
                           :output-dir "resources/public/js/compiled/test"}}


               ;; This next build is an compressed minified build for
               ;; production. You can build this with:
               ;; lein cljsbuild once min
               {:id "min"
                :source-paths ["src"]
                :compiler {:output-to "resources/public/js/compiled/core.js"
                           :main app.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             :server-port 3000 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler
             :ring-handler server.core/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             }

  :repl-options  {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]
                  :init (do
                          (use 'figwheel-sidecar.repl-api)
                          (start-figwheel!)
                          (println "Run (cljs-repl) to connect to the cljs repl"))})
