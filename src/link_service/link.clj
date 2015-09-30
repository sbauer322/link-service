(ns link-service.link
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [cheshire.core :as json]
            [environ.core :refer [env]]
            [clojure.string :as s]
            [link-service.db :as db]))

(def tokens
  (let [tkns (env :tokens)]
    (if (coll? tkns)
      tkns
      (s/split tkns #","))))

(defn valid-token?
  "Returns true if token is valid. Otherwise nil."
  [token]
  (info "Validating token: " token " against tokens: " tokens)
  (some #(= % token) tokens))

(def invalid-token
  {:success? false :message "Invalid token!"})

(defn response
  [success? message]
  {:success? success? :message message})

(defn random-link
  "Returns a random link from the collection."
  []
  (let [result (:link (db/random-link))]
    (if (nil? result)
      (response false "No link found.")
      (response true result))))

(defn add-link
  "If the token is valid and the link is not already in the list then add the link to the list. Otherwise returns nil."
  [token new-link]
  (if (valid-token? token)
    (do
      (let [result (db/add-link new-link)]
        (if (nil? result)
          (response false (str "Failed to add link: " new-link))
          (response true (str "Added link: " new-link)))))
    invalid-token))

(defn get-link
  "Returns a JSON string of the link retreived from the database or an error string. If the link is not found then an empty JSON string is returned."
  [token link]
  (if (valid-token? token)
    (let [result (:link (db/get-link link))]
      (if (nil? result)
        (response false "No link found.")
        (do
          (debug "Returning link: " link)
          (response true result))))
    invalid-token))

(defn delete-link
  "Attempts to delete the specified link."
  [token link]
  (if (valid-token? token)
    (do
      (db/delete-link link)
      (let [result (get-link token link)]
        (if (:success? result)
          (response false (str "Failed to delete link: " link))
          (response true (str "Deleted link: " link)))))
    invalid-token))


