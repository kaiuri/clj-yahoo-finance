(ns kaiuri.yahoo-finance
  (:require [clojure.string :as string])
  (:import [java.net URLEncoder]
           [java.time LocalDate ZoneId]))

(set! *warn-on-reflection* true)

(defn- str->milli
  [s]
  (let [date (LocalDate/parse s)
        zone (ZoneId/systemDefault)
        milli (-> date
                  (.atStartOfDay zone)
                  .toInstant
                  .toEpochMilli)]
    (quot milli 1000)))

(defn- query1-v7
  "PRIVATE: Uses the current (12-07-22) finance.yahoo.com routing to get historical data."
  [{:keys [stock
           ^clojure.lang.PersistentVector period
           frequency]}]
  (let [stock-chunk (URLEncoder/encode stock)
        period-chunk (->> period
                          (map (fn [p]
                                 (let [idx (+ 1 (.indexOf period p))]
                                   (str "period" idx \= (str->milli p)))))
                          (string/join \&))
        frequency-chunk (str \1 frequency)]
    (str "https://query1.finance.yahoo.com/v7/finance/download/" stock-chunk
         \? period-chunk
         "&interval=" frequency-chunk
         "&events=history"
         "&includeAdjustedClose=true")))

(defn- csv->clj
  "PRIVATE: Subject to changes.
   Parses CSV string into vector of dicts.
    - <s>: CSV string.
    - [del]: Delimiter, defaults to `,`

  "
  [s & {:keys [del] :or {del #","}}]
  (let [csv (string/split-lines s)
        head (map keyword (string/split (first csv) del))
        body (map #(string/split % del) (rest csv))]
    (mapv #(zipmap head %) body)))

(defn get-historical
  "Takes a dict with the following keys:
    - <stock>: Stock's name (string) as displayed in `finance.yahoo.com`.
    - <period>: PersistentVector tuple of date strings, each in the format \"YYYY-MM-dd\".
    - [frequency]: \"w\"(week), \"d\"(day) or \"m\"(month).
    - [extension]: One of `:clojure`, `:raw`, defaults to `:clojure`.

   Returns historical data in the specified extension.
   "

  [{:keys [stock
           ^clojure.lang.PersistentVector period
           frequency
           extension]
    :or {frequency "d" extension :clojure}}]
  (let [raw-data (->> {:stock stock
                       :period period
                       :frequency frequency}
                      (query1-v7)
                      (slurp))]
    (case extension
      :raw raw-data
      :clojure (csv->clj raw-data)
      :else (csv->clj raw-data))))
