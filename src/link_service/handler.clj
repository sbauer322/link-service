(ns link-service.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [link-service.link :as link]))

(defroutes app-routes
  (GET "/random" [] (link/random))
  (GET "/add/:user-token/:new-link" [user-token new-link] (link/add-link user-token new-link))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)))
