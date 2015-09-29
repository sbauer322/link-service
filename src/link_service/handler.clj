(ns link-service.handler
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
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
                                  link (:link body)]
                              (response (link/add-link token link))))
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

(defn -main
  "Starts the server, ensuring that the database has been properly
  initialized. Starts on port 8090."
  [& args]
  (init)
  (run-server app {}))
