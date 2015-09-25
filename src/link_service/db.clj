(ns link-service.db
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [korma.db :refer :all]
            [korma.core :refer :all]
            [lobos.config :as config]
            [lobos.core :refer [migrate]]))

(do
  (migrate)
  )

;; Pass the connection map to the defdb macro:
(defdb korma-db config/db)

;;;; Define tables for korma. Korma does not create tables but lobos does... see lobos/migrations.clj.

(defentity links
  (pk :id) ; Short for primary key, the line is unnecessary in this example.
  (entity-fields :id :link :dead :created_on))

(defentity counts
  (pk :id)
  (entity-fields :id :count))

(defentity user-tokens
  (pk :id)
  (entity-fields :id :user-token))

;;;; Retreive from database

(defn get-link
  [link]
  (first (select links
                 (where {:link link}))))

(defn get-link-index
  "Returns the link from the specified index or nil."
  [i]
  (first (select links
                 (where {:id i}))))

(defn get-token
  "Returns the token if a match is found. Otherwise, returns nil.
  Will throw an exception if the requested token is not at least one character long."
  [token]
  (first (select user-tokens
                 (where {:user-token token}))))

(defn random-link
  "Return a single random link from the links table.

  This may turn out to be inefficient for large tables, but it is good enough for now."
  []
  (exec-raw ["SELECT \"links\".* FROM \"links\" ORDER BY RAND() LIMIT 1"] :results))

;;;; Utility

(defn valid-new-input?
  "Confirms a link or token to be added to the database is acceptable.

  In particular:
  * Not empty
  * Not nil
  * Not already in the database"
  [input table-fn]
  (if (not (or (nil? input) (empty? input)))
    (let [before (table-fn input)]
      (if (nil? before)
        true
        false))
    false))

;;;; Add to database

(defn add-link
  "Add the link if it is valid and not already in the table."
  [link]
  (if (valid-new-input? link get-link)
    (do
      (debug "Adding link: " link)
      (insert links
              (values {:link link :dead false})))
    nil))

(defn add-token
  "Adds the token if it is valid and not already in the table."
  [token]
  (if (valid-new-input? token get-token)
    (do
      (debug "Adding token: " token)
      (insert user-tokens
              (values {:user-token token})))
    nil))

;;;; Remove from database

(defn remove-link
  "Remove the link if it is valid and can be found in the table."
  [link]
  (let [before (get-link link)]
    (when-not (nil? before)
      (delete links
              (where {:link link})))))
