(ns link-service.db
  (:require [taoensso.timbre :as timbre :refer (log  trace  debug  info  warn  error  fatal  report spy)]
            [korma.db :refer :all]
            [korma.core :refer :all]
            [lobos.config :as config]
            [lobos.connectivity :as conn]
            [lobos.core :refer [migrate]]
            [clj-time.core :as t]
            [clj-time.format :as f]))

(defn init
  "Initializes the database and opens the connection."
  []
  (conn/open-global config/db)
  ; We do the following binding because the migrations won't execute in an uberjar. See http://stackoverflow.com/a/15331451/3141194
  (binding [lobos.migration/*reload-migrations* false]
    (migrate))
  )

;; Pass the connection map to the defdb macro:
(defdb korma-db config/db)

;;;; Define tables for korma. Korma does not create tables but lobos does... see lobos/migrations.clj.

(defentity links
  (pk :id) ; Short for primary key, the line is unnecessary in this example.
  (entity-fields :id :link :dead :created_on))

;;;; Retreive from database

(defn get-link
  "Returns the link, or nil if not found. If there are multiple links returned only the first is returned as the result."
  [link]
  (first (select links
                 (where {:link link}))))

(defn get-link-index
  "Returns the link from the specified index or nil."
  [i]
  (first (select links
                 (where {:id i}))))

(defn random-link
  "Return a single random link from the links table.

  This may turn out to be inefficient for large tables, but it is good enough for now."
  []
  (first (exec-raw ["SELECT \"links\".* FROM \"links\" ORDER BY RAND() LIMIT 1"] :results)))

;;;; Utility

(defn valid-new-input?
  "Confirms a link to be added to the database is acceptable.

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

(def format-time (f/formatters :mysql))

;;;; Add to database

(defn add-link
  "Add the link if it is valid and not already in the table."
  [link]
  (if (valid-new-input? link get-link)
    (do
      (debug "Adding link: " link)
      (insert links
              (values {:link link :dead false :created_on (f/unparse format-time (t/now))})))
    nil))

;;;; Delete from database

(defn delete-link
  "Delete the link if it is valid and can be found in the table. Returns nil regardless if the link was in the table or not."
  [link]
  (let [before (get-link link)]
    (when-not (nil? before)
      (delete links
              (where {:link link})))))
