(ns kaiuri.clj-yahoo-finance.core
  (:require
    [clojure.spec.alpha :as s]
    [kaiuri.clj-yahoo-finance.specs]
    [kaiuri.clj-yahoo-finance.utils :refer [csv->clj gregorian->unix]])
  (:import
    [java.net URLEncoder]))

(set! *warn-on-reflection* true)

(defn- query1-v7
  "PRIVATE: Uses the current (12-07-22) finance.yahoo.com routing to get historical data."
  [{:keys [stock
           ^clojure.lang.PersistentVector period
           frequency]}]
  (let [stock-chunk (URLEncoder/encode stock)
        period-chunk (gregorian->unix period)
        frequency-chunk (str \1 frequency)]
    (str "https://query1.finance.yahoo.com/v7/finance/download/" stock-chunk
         \? period-chunk
         "&interval=" frequency-chunk
         "&events=history"
         "&includeAdjustedClose=true")))

(defn get-historical
  "Takes a dict with the following keys:
    - <stock>: Stock's name (string) as displayed in `finance.yahoo.com`.
    - <period>: PersistentVector tuple of date strings, each in the format `YYYY-MM-dd`.
    - [frequency]: `w`(week), `d`(day) or `m`(month).
    - [extension]: One of `:clojure`, `:raw`, defaults to `:clojure`.

   Returns historical data in the specified extension.
   "
  [{:keys [stock
           ^clojure.lang.PersistentVector period
           frequency
           extension]
    :or {frequency "d" extension :clojure}}]

  {:pre [(s/valid? :query/period period)
         (s/valid? :query/frequency frequency)
         (s/valid? :query/extension extension)]}

  (let [raw-data (->> {:stock stock
                       :period period
                       :frequency frequency}
                      (query1-v7)
                      (slurp))]
    (case extension
      :raw raw-data
      :clojure (csv->clj raw-data)
      :else (csv->clj raw-data))))
