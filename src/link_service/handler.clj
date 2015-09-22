(ns link-service.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [link-service.db :as db]
            [link-service.link :as link]))

(defroutes app-routes
  (GET "/random" [] (link/random-link))
  ;; These two GETs will need to be removed in the future as it is not feasible to have a link within the route. Currently left for debugging.
  (GET "/add/:user-token/:new-link" [user-token new-link] (link/add-link user-token new-link))
  (GET "/get/:user-token/:link" [user-token link] (link/get-link user-token link))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
   (handler/site app-routes)))
