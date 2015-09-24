(ns link-service.link
  (:require [cheshire.core :as json]
            [link-service.db :as db]))

(def user-tokens
  ; For debugging. To be removed in the future.
  ["sbauer"])

(def links
  ; For debugging. To be removed in the future.
  ["http://29.media.tumblr.com/tumblr_lsvcpxVBgd1qzgqodo1_500.jpg"])

(defn valid-token?
  [token]
  (some #(= % token) user-tokens))

(def invalid-token
  "Invalid token!")

(defn add-link-success
  [link]
  (str "Successfully added the following link to the service: " link))

(defn random-link
  "Returns a random link from the collection."
  ([]
   (json/generate-string (db/random-link))))

(defn add-link
  "If the user-token is valid and the link is not already in the list then add the link to the list."
  [user-token new-link]
  (if (valid-token? user-token)
    (do
      (db/add-link new-link)
      (add-link-success new-link)
      )
    invalid-token))

(defn get-link
  "Returns a json string of the link retreived from the database or an error string."
  [user-token link]
  (if (valid-token? user-token)
    (do
      (json/generate-string (db/get-link link))
      )
    invalid-token))


