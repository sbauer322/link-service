(ns link-service.link
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [cheshire.core :as json]
            [link-service.db :as db]))

(def tokens
  ; For debugging. To be removed in the future.
  ["sbauer"])

(defn valid-token?
  [token]
  (debug "Validating token: " token)
  (some #(= % token) tokens))

(def invalid-token
  "Invalid token!")

(defn add-link-success
  [link]
  (str "Successfully added the following link to the service: " link))

(defn random-link
  "Returns a random link from the collection."
  ([]
   (select-keys (into {} (db/random-link)) [:link])))

(defn add-link
  "If the token is valid and the link is not already in the list then add the link to the list."
  [token new-link]
  (if (valid-token? token)
    (do
      (let [result (db/add-link new-link)]
        (if (nil? result)
          "Failed to add link."
          (add-link-success new-link))))
    invalid-token))

(defn get-link
  "Returns a json string of the link retreived from the database or an error string."
  [token link]
  (if (valid-token? token)
    (do
      (debug "Returning link: " link)
      (db/get-link link)
      )
    invalid-token))
