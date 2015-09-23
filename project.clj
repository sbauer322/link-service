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
                 [compojure "1.3.1"]
                 [ring/ring-defaults "0.1.2"]
                 [cheshire "5.5.0"] ;; Handles JSON
                 [org.clojure/java.jdbc "0.3.7"] ;; For Korma
                 [korma "0.4.0"] ;; DSL for RMDB
                 [com.h2database/h2 "1.3.170"] ;; JDBC driver for H2
                 [lobos "1.0.0-beta3"] ;; For SQL database schema manipulation and migration... primarily handles create, alter, and drop.
                 ]

  :plugins [[codox "0.8.13"]
            [lein-ring "0.8.13"]]

  :codox {:defaults {:doc "FIXME: write docs"
                     :doc/format :markdown}
          :output-dir "doc"}

  :ring {:handler link-service.handler/app}

  ;;; Entry Point
  :main link-service.handler

  ;;; Profiles
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}

  ;;; Repl
  :repl-options {:init-ns link-service.handler})
