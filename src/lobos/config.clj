(ns lobos.config
  (:require [clojure.string :as string]
            [environ.core :refer [env]]))

(def db
  {:db          "resources/db/link-service.db;PASSWORD_HASH=TRUE"
   :subname     "resources/db/link-service.db;PASSWORD_HASH=TRUE"
   :classname   "org.h2.Driver"
   :subprotocol "h2"
   :user        (or (env :db-user) "sa")
   :password    (or (env :db-password) "")
   :naming {:keys string/lower-case
            ;; set map keys to lower
            :fields string/lower-case}})
