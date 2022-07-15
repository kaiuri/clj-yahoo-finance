(ns kaiuri.clj-yahoo-finance.utils
  (:require [camel-snake-kebab.core :as csk]
            [clojure.spec.alpha :as s]
            [clojure.string :as string]
            [kaiuri.clj-yahoo-finance.specs])
  (:import [java.time LocalDate ZoneId]))

(set! *warn-on-reflection* true)

(defn- str->milli
  [s]
  {:pre [(s/valid? :query/time s)]}
  (let [date (LocalDate/parse s)
        zone (ZoneId/systemDefault)
        milli (-> date
                  (.atStartOfDay zone)
                  .toInstant
                  .toEpochMilli)]
    (quot milli 1000)))

(defn csv->clj
  "PRIVATE: Subject to changes.
   Parses CSV string into vector of dicts.
    - <s>: CSV string.
    - [del]: Delimiter, defaults to `,`

  "
  [s & {:keys [del] :or {del #","}}]
  (let [csv (string/split-lines s)
        head (map #(keyword (csk/->kebab-case %)) (string/split (first csv) del))
        body (map #(string/split % del) (rest csv))]
    (mapv #(zipmap head %) body)))

(defn gregorian->unix
  [^clojure.lang.PersistentVector period]
  {:pre [(s/valid? :query/period period)]}
  (->> period
       (map (fn [p]
              (let [idx (+ 1 (.indexOf period p))]
                (str "period" idx \= (str->milli p)))))
       (string/join \&)))
