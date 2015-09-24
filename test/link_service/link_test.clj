(ns link-service.link-test
  (:require [clojure.test :refer :all]
            [link-service.link :refer :all]
            [link-service.db :as db]))

(deftest add-link-test
  (with-redefs [db/add-link (fn [link] 1)
                user-tokens ["user"]]
    (is (= (add-link "user" "foo") (add-link-success "foo")))))

(deftest get-link-test
  (with-redefs [db/get-link (fn [link] link)
                user-tokens ["user"]]
    (is (= (get-link "user" "foo") "\"foo\""))
    (is (= (get-link "not-user" "foo") invalid-token))))
