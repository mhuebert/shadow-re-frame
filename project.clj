(defproject shadow-re-frame "0.1.0-SNAPSHOT"

            :url "https://www.github.com/re-view/re-view"

            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}

            :min-lein-version "2.7.1"

            :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                           [org.clojure/clojurescript "1.9.946"]

                           [re-view "0.4.6"]
                           [re-frame "0.10.2"]
                           [braintripping/re-frame-trace "0.1.7-5" :exclusions [re-frame]]
                           [cljsjs/d3 "4.3.0-5"]
                           [binaryage/devtools "0.9.7"]
                           [re-view/re-frame-simple "0.1.1"]]

            :plugins [[lein-cljsbuild "1.1.7"]
                      [lein-figwheel "0.5.14"]]

            :source-paths ["src"]

            :figwheel {:ring-handler figwheel-server.core/handler
                       :server-port  5300}

            :profiles {:dev {:dependencies [[figwheel-pushstate-server "0.1.2"]]}}

            :cljsbuild {:builds [{:id           "dev"
                                  :source-paths ["src"]

                                  ;; change this to `:figwheel true` to stop figwheel from
                                  ;; automatically opening the page after compile.
                                  :figwheel     {:open-urls ["http://localhost:5300/"]}

                                  :compiler     {:main           "shadow-re-frame.simple"
                                                 :install-deps   true
                                                 :parallel-build true
                                                 :infer-externs  true
                                                 :source-map     true
                                                 :asset-path     "/js/compiled/out-dev"
                                                 :output-to      "docs/js/compiled/base.js"
                                                 :output-dir     "docs/js/compiled/out-dev"
                                                 :optimizations  :none
                                                 :preloads        [devtools.preload
                                                                   day8.re-frame.trace.preload]}}

                                 {:id           "prod"
                                  :source-paths ["src"]
                                  :compiler     {:main           "shadow-re-frame.simple"
                                                 :install-deps   true
                                                 :infer-externs  true
                                                 :parallel-build true
                                                 :closure-defines  {re-frame.trace.trace-enabled? true}
                                                 :closure-warnings {:global-this :off}
                                                 :output-to      "docs/js/compiled/base.js"
                                                 :output-dir     "docs/js/compiled/out-prod"
                                                 :optimizations  :advanced}}]}

            :eval-in-leiningen true)
