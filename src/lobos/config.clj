(ns lobos.config
  (:require [clojure.string :as string]
            [lobos.connectivity :as conn]))

(def db
  {:db          "resources/db/korma.db"
   :subname     "resources/db/korma.db"
   :classname   "org.h2.Driver"
   :subprotocol "h2"
   :user        "sa"
   :password    ""
   :naming {:keys string/lower-case
            ;; set map keys to lower
            :fields string/lower-case}})

(conn/open-global db)
