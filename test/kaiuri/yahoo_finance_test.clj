(ns kaiuri.yahoo-finance-test
  (:require
    [clojure.test :as t :refer [deftest is testing]]
    [kaiuri.clj-yahoo-finance.core :as yf]))

(deftest get-historical-clojure
  (testing "kaiuri.clj-yahoo-finance.core/get-historical"
    (let [query {:stock "CL=F"
                 :period ["2022-01-01" "2022-02-01"]
                 :frequency "d"
                 :extension :clojure}
          data (yf/get-historical query)]
      (is some? data)
      (is some? (take 2 data)))))

(deftest get-historical-csv
  (testing "kaiuri.clj-yahoo-finance.core/get-historical"
    (let [query {:stock "^IXIC"
                 :period ["2022-01-01" "2022-02-01"]
                 :frequency "d"
                 :extension :csv}
          data (yf/get-historical query)]
      (is some? data)
      (is some? (take 2 data)))))

(deftest get-historical-yaml
  (testing "kaiuri.clj-yahoo-finance.core/get-historical"
    (let [query {:stock "^RUT"
                 :period ["2022-01-01" "2022-02-01"]
                 :frequency "d"
                 :extension :yaml}
          data (yf/get-historical query)]
      (is some? data)
      (is some? (take 2 data)))))

(deftest get-historical-json
  (testing "kaiuri.clj-yahoo-finance.core/get-historical"
    (let [query {:stock "^RUT"
                 :period ["2022-01-01" "2022-02-01"]
                 :frequency "d"
                 :extension :json}
          data (yf/get-historical query)]
      (is some? data)
      (is some? (take 2 data)))))
