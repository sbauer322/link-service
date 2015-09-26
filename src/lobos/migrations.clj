(ns lobos.migrations
  (:refer-clojure :exclude [alter drop
                            bigint boolean char double float time])
  (:require [lobos.schema :as s]
            [lobos.core :as c]
            [lobos.helpers :as helpers]
            [lobos.migration :refer [defmigration]]))

;;;; Create tables. These should match with the ones found in db for Korma.

(defmigration add-links-table
  (up [] (c/create
          (helpers/tbl :links
                       (s/integer :id :primary-key :auto-inc :unique)
                       (s/varchar :link 255 :unique :not-null)
                       (s/check :link (> (length :link) 1))
                       (s/boolean :dead)
                       (s/timestamp :created_on))))
  (down [] (c/drop (s/table :links))))

;;;; Examples from lobos README
;(defmigration add-users-table
;  (up [] (c/create
;          (helpers/tbl :users
;                       (s/varchar :name 100 :unique)
;                       (s/check :name (> (length :name) 1)))))
;  (down [] (c/drop (s/table :users))))
;
;(defmigration add-posts-table
;  (up [] (c/create
;          (helpers/tbl :posts
;                       (s/varchar :title 200 :unique)
;                       (s/text :content)
;                       (helpers/refer-to :users))))
;  (down [] (c/drop (s/table :posts))))
;
;(defmigration add-comments-table
;  (up [] (c/create
;          (helpers/tbl :comments
;                       (s/text :content)
;                       (helpers/refer-to :users)
;                       (helpers/refer-to :posts))))
;  (down [] (c/drop (s/table :comments))))
