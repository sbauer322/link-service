(ns link-service.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [link-service.handler :refer :all]
            [link-service.db :as db]))

(deftest app-test
  (testing "random"
    (with-redefs [db/random-link (fn [] {:link "foo"})]
      (let [response (app (mock/request :get "/random"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"link\":\"foo\"}")))))

  (testing "default route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 404))
      (is (= (:body response) "Not Found"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
