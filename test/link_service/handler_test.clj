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

  (testing "add link - bad token"
    (with-redefs [db/add-link (fn [_] {:link "foo"})]
      (let [response (app (mock/body
                           (mock/content-type
                            (mock/request :post "/add")
                            "application/json")
                           "{\"link\": \"https://agithusb.com/ring-csdflojure/ring\", \"token\": \"nonuser\"}"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"success?\":false,\"message\":\"Invalid token!\"}")))))

  (testing "add link - good token - bad link"
    (with-redefs [db/add-link (fn [_] nil)]
      (let [response (app (mock/body
                           (mock/content-type
                            (mock/request :post "/add")
                            "application/json")
                           "{\"link\": \"\", \"token\": \"10f27b0f\"}"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"success?\":false,\"message\":\"Failed to add link: \"}")))))

  (testing "add link - good token - good link"
    (with-redefs [db/add-link (fn [_] {:link "https://github.com/"})]
      (let [response (app (mock/body
                           (mock/content-type
                            (mock/request :post "/add")
                            "application/json")
                           "{\"link\": \"https://github.com/\", \"token\": \"10f27b0f\"}"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"success?\":true,\"message\":\"Added link: https://github.com/\"}")))))

  (testing "delete link - bad token"
    (with-redefs [db/delete-link (fn [_] nil)
                  db/get-link (fn [_] nil)]
      (let [response (app (mock/body
                           (mock/content-type
                            (mock/request :post "/delete")
                            "application/json")
                           "{\"link\": \"https://github.com/\", \"token\": \"nonuser\"}"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"success?\":false,\"message\":\"Invalid token!\"}")))))

  (testing "delete link - good token - bad link"
    (with-redefs [db/delete-link (fn [_] nil)
                  db/get-link (fn [_] nil)]
      (let [response (app (mock/body
                           (mock/content-type
                            (mock/request :post "/delete")
                            "application/json")
                           "{\"link\": \"\", \"token\": \"10f27b0f\"}"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"success?\":true,\"message\":\"Deleted link: \"}")))))

  (testing "delete link - good token - good link"
    (with-redefs [db/delete-link (fn [_] nil)
                  db/get-link (fn [_] nil)]
      (let [response (app (mock/body
                           (mock/content-type
                            (mock/request :post "/delete")
                            "application/json")
                           "{\"link\": \"https://github.com/\", \"token\": \"10f27b0f\"}"))]
        (is (= (:status response) 200))
        (is (= (:body response) "{\"success?\":true,\"message\":\"Deleted link: https://github.com/\"}")))))

  (testing "default route"
    (let [response (app (mock/request :get "/"))]
      (is (= (:status response) 404))
      (is (= (:body response) "Not Found"))))

  (testing "not-found route"
    (let [response (app (mock/request :get "/invalid"))]
      (is (= (:status response) 404)))))
