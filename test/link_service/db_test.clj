(ns link-service.db-test
  (:require [clojure.test :refer :all]
            [link-service.db :refer :all]))

(deftest valid-new-input?-test
  (testing "nil"
    (with-redefs [get-link (fn [link] link)
                  get-token (fn [token] token)]
      (is (= (valid-new-input? nil get-link) false))
      (is (= (valid-new-input? nil get-token) false))))
  (testing "empty string"
    (with-redefs [get-link (fn [link] link)
                  get-token (fn [token] token)]
      (is (= (valid-new-input? "" get-link) false))
      (is (= (valid-new-input? "" get-token) false))))
  (testing "already in db"
    (with-redefs [get-link (fn [link] link)
                  get-token (fn [token] token)]
      (is (= (valid-new-input? "foo" get-link) false))
      (is (= (valid-new-input? "foo" get-token) false))))
  (testing "valid"
    (with-redefs [get-link (fn [_] nil)
                  get-token (fn [_] nil)]
      (is (= (valid-new-input? "foo" get-link) true))
      (is (= (valid-new-input? "foo" get-token) true)))))
