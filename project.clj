(defproject link-service "0.1.0-SNAPSHOT"
  ;;; Project Metadata
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"

  ;;; Filesystem Paths
  :source-paths ["src"]
  :test-paths ["test"]
  :resource-paths ["resources"]
  :target-path "target/%s"

  ;;; Dependencies, Plugins, and Repositories
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.taoensso/timbre "4.0.2"] ;; Logging
                 [environ "1.0.0"] ;; Manages environment settings
                 [compojure "1.3.1"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-defaults "0.1.2"]
                 [ring/ring-json "0.4.0"]
                 [http-kit "2.1.19"]
                 [cheshire "5.5.0"] ;; Handles JSON
                 [org.clojure/java.jdbc "0.3.7"] ;; For Korma
                 [korma "0.4.0"] ;; DSL for RMDB
                 [com.h2database/h2 "1.3.170"] ;; JDBC driver for H2
                 [lobos "1.0.0-beta3"] ;; For SQL database schema manipulation and migration... primarily handles create, alter, and drop.
                 [clj-time "0.11.0"]]

  :plugins [[codox "0.8.13"]
            [lein-environ "1.0.0"]
            [lein-ring "0.8.13"]]

  :codox {:defaults {:doc/format :markdown}
          :output-dir "doc"}

  :ring {:handler link-service.handler/app}

  ;;; Entry Point
  :main link-service.handler

  ;;; Profiles
  :profiles {:dev-common {:dependencies [[javax.servlet/servlet-api "2.5"]
                                         [ring-mock "0.3.0"]]}
             :dev-overrides {}
             :dev [:dev-common :dev-overrides]

             :test-common {:dependencies [[ring-mock "0.3.0"]]}
             :test-overrides {}
             :test [:test-common :test-overrides]

             :uberjar-common {:omit-source true
                              :aot :all}
             :uberjar-overrides {}
             :uberjar [:uberjar-common :uberjar-overrides]}

  ;;; Repl
  :repl-options {:init-ns link-service.handler})
