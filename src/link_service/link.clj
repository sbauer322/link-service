(ns link-service.link
  (:require [cheshire.core :as json]
            [link-service.db :as db]))

(def user-tokens
  ; For debugging. To be removed in the future.
  ["sbauer"])

(def links
  ; For debugging. To be removed in the future.
  ["http://29.media.tumblr.com/tumblr_lsvcpxVBgd1qzgqodo1_500.jpg"])

(defn random-link
  "Returns a random link from the collection."
  ([]
   (json/generate-string (db/random-link))))

(defn add-link
  "If the user-token is valid and the link is not already in the list then add the link to the list."
  [user-token new-link]
  (if (some #(= % user-token) user-tokens)
    (do
      (db/add-link new-link)
      (str "Successfully added the following link to the service: " new-link)
      )
    (str "The following link was not added as the user token is invalid: " new-link)))

(defn get-link
  [user-token link]
  (if (some #(= % user-token) user-tokens)
    (do
      (json/generate-string (db/get-link link))
      )
    (str "The following link was not requested as the user token is invalid: " link)))
