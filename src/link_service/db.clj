(ns link-service.db
  (:require [korma.db :refer :all]
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

;;;; Database API

(defn random-link
  "Return a single random link from the links table.

  This may turn out to be inefficient for large tables, but it is good enough for now."
  []
  (exec-raw ["SELECT \"links\".* FROM \"links\" ORDER BY RAND() LIMIT 1"] :results))

(defn link-count
  "Provides an up-to-date count of how many links are in the link table."
  []
  (let [cnt (select counts
                    (where {:id 1}))]
    (when (empty? cnt)
      (insert counts
              (values {:id 1 :count 0})))
    (:count (first cnt) 0)))

(defn get-user-token
  "Returns the user token if a match is found. Otherwise, returns nil.
  Will throw an exception if the requested token is not at least one character long."
  [token]
  (first (select user-tokens
                 (where {:user-token token}))))

(defn add-user-token
  "Adds the token if it is valid and not already in the table."
  [token]
  (let [before (get-user-token token)]
    (when (nil? before)
      (insert user-tokens
              (values {:user-token token})))))

(defn get-link
  [link]
  (first (select links
                 (where {:link link}))))

(defn get-link-index
  "Returns the link from the specified index or nil."
  [i]
  (first (select links
                 (where {:id i}))))

(defn add-link
  "Add the link if it is valid and not already in the table."
  [link]
  (let [before (get-link link)]
    (when (nil? before)
      (insert links
              (values {:link link :dead false}))
      (update counts ; Increment link count.
              (set-fields {:count (inc (link-count))})
              (where {:id 1})))))

(defn remove-link
  "Remove the link if it is valid and can be found in the table."
  [link]
  (let [before (get-link link)]
    (when-not (nil? before)
      (delete links
              (where {:link link})))))
