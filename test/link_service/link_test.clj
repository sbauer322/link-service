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
    (is (= (valid-token? "not-user") nil)))
  (testing "valid"
    (is (= (valid-token? "10f27b0f") true))))

(deftest add-link-test
  (with-redefs [db/add-link (fn [link] 1)]
    (is (= (add-link "10f27b0f" "foo") {:success? true, :message "Added link: foo"}))))

(deftest get-link-test
  (with-redefs [db/get-link (fn [link] {:link link})]
    (is (= (get-link "10f27b0f" "foo") {:success? true, :message "foo"}))
    (is (= (get-link "not-user" "foo") {:success? false, :message "Invalid token!"}))))

(deftest delete-link-test
  (with-redefs [db/delete-link (fn [_] nil)
                db/get-link (fn [_] nil)]
    (is (= (delete-link "10f27b0f" "foo") {:success? true, :message "Deleted link: foo"}))
    (is (= (delete-link "not-user" "foo") {:success? false, :message "Invalid token!"}))))
