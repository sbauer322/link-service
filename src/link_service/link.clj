(ns link-service.link
  (:require [cheshire.core :as json]))

(def user-tokens
  ["sbauer"])

(def links
  ["http://29.media.tumblr.com/tumblr_lsvcpxVBgd1qzgqodo1_500.jpg"])

(defn random
  "Returns a random link from the collection."
  []
  (json/generate-string {:link (get links (rand-int (count links)))}))

(defn add-link
  "If the user-token is valid and the link is not already in the list then add the link to the list."
  [user-token new-link]
  (if (some #(= % user-token) user-tokens)
    (str "Successfully added the following link to the service: " new-link)
    (str "The following link was not added as the user token is invalid: " new-link)))
