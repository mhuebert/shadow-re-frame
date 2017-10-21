(defproject shadow-re-frame "0.1.0-SNAPSHOT"

            :url "https://www.github.com/re-view/re-view"

            :license {:name "Eclipse Public License"
                      :url  "http://www.eclipse.org/legal/epl-v10.html"}

            :min-lein-version "2.7.1"

            :dependencies [[org.clojure/clojure "1.9.0-alpha17"]
                           [org.clojure/clojurescript "1.9.946"]

                           [re-view "0.4.6"]
                           [re-frame "0.9.0"]
                           [braintripping/re-frame-trace "0.1.8"]
                           [binaryage/devtools "0.9.7"]]

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

                                  :compiler     {:main           "shadow-re-frame.core"
                                                 :install-deps   true
                                                 :parallel-build true
                                                 :infer-externs  true
                                                 :source-map     true
                                                 :asset-path     "/js/compiled/out-dev"
                                                 :output-to      "resources/public/js/compiled/base.js"
                                                 :output-dir     "resources/public/js/compiled/out-dev"
                                                 :optimizations  :none
                                                 :preloads        [devtools.preload
                                                                   day8.re-frame.trace.preload]}}

                                 {:id           "prod"
                                  :source-paths ["src"]
                                  :compiler     {:main           "shadow-re-frame.core"
                                                 :install-deps   true
                                                 :infer-externs  true
                                                 :parallel-build true
                                                 :output-to      "resources/public/js/compiled/base.js"
                                                 :output-dir     "resources/public/js/compiled/out-prod"
                                                 :optimizations  :advanced}}]}

            :eval-in-leiningen true)
