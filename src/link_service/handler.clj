(ns link-service.handler
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]
            [link-service.db :as db]
            [link-service.link :as link]))

(defroutes app-routes
  (GET "/random" [] (response (link/random-link)))
  ;; These two GETs will need to be removed in the future as it is not feasible to have a link within the route. Currently left for debugging.
  (POST "/add" {body :body} (let [token (:token body)
                                  link (:link body)]
                              (response (link/add-link token link))))


  ; (GET "/add/:user-token/:new-link" [user-token new-link] (link/add-link user-token new-link))
  ; (GET "/get/:user-token/:link" [user-token link] (link/get-link user-token link))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (->
   (handler/api app-routes)
   (wrap-json-body {:keywords? true})
   (wrap-json-response)))
