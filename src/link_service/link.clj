(ns link-service.link
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [cheshire.core :as json]
            [environ.core :refer [env]]
            [link-service.db :as db]))

(def tokens
  (env :tokens))

(defn valid-token?
  "Returns true if token is valid. Otherwise nil."
  [token]
  (debug "Validating token: " token)
  (some #(= % token) tokens))

(def invalid-token
  {:success? false :message "Invalid token!"})

(defn add-link-success
  [link]
  {:success? true :message (str "Added link: " link)})

(defn get-link-success
  [m]
  (select-keys (into {} m) [:link]))

(defn random-link
  "Returns a random link from the collection."
  ([]
   (get-link-success (db/random-link))))

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
      (get-link-success (db/get-link link))
      )
    invalid-token))
