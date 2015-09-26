(ns link-service.db-test
  (:require [clojure.test :refer :all]
            [link-service.db :refer :all]))

(deftest valid-new-input?-test
  (testing "nil"
    (with-redefs [get-link (fn [link] link)]
      (is (= (valid-new-input? nil get-link) false))))
  (testing "empty string"
    (with-redefs [get-link (fn [link] link)]
      (is (= (valid-new-input? "" get-link) false))))
  (testing "already in db"
    (with-redefs [get-link (fn [link] link)]
      (is (= (valid-new-input? "foo" get-link) false))))
  (testing "valid"
    (with-redefs [get-link (fn [_] nil)]
      (is (= (valid-new-input? "foo" get-link) true)))))
