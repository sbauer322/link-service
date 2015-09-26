(ns link-service.link-test
  (:require [clojure.test :refer :all]
            [link-service.link :refer :all]
            [link-service.db :as db]))

(deftest valid-token?-test
  (testing "emtpy"
    (is (= (valid-token? "") nil)))
  (testing "nil"
    (is (= (valid-token? nil) nil)))
  (testing "not valid"
    (is (= (valid-token? "richard") nil)))
  (testing "valid"
    (is (= (valid-token? "user") true))))

(deftest add-link-test
  (with-redefs [db/add-link (fn [link] 1)]
    (is (= (add-link "user" "foo") (add-link-success "foo")))))

(deftest get-link-test
  (with-redefs [db/get-link (fn [link] link)]
    (is (= (get-link "user" "foo") "foo"))
    (is (= (get-link "not-user" "foo") invalid-token))))
