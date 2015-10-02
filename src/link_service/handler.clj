(ns link-service.handler
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [environ.core :refer [env]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]
            [org.httpkit.server :refer [run-server]]
            [link-service.db :as db]
            [link-service.link :as link])
  (:gen-class))

(defroutes app-routes
  (GET "/random" [] (response (link/random-link)))
  (POST "/add" {body :body} (let [token (:token body)
                                  link (:message body)]
                              (response (link/add-link token link))))
  (POST "/delete" {body :body} (let [token (:token body)
                                     link (:message body)]
                                 (response (link/delete-link token link))))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
   (handler/api app-routes)
   (wrap-json-body {:keywords? true})
   (wrap-json-response)))

(defn init
  []
  (db/init))

(defonce server (atom nil))

(defn stop-server
  "Stop the server gracefully."
  []
  (when-not (nil? @server)
    ;; graceful shutdown: wait 100ms for existing requests to be finished
    ;; :timeout is optional, when no timeout, stop immediately
    (@server :timeout 100)
    (reset! server nil)
    (debug "Server stopped.")))

(defn -main
  "Starts the server, ensuring that the database has been properly
  initialized. Server defaults to ip 0.0.0.0 and port 7999 if not specified."
  [& args]
  (init)
  (let [ip (or (env :ip) "0.0.0.0")
        port (or (bigdec (env :port)) 7999)]
    (reset! server (run-server app {:ip ip
                                    :port port}))
    (debug "Server started at: " ip ":" port)))
