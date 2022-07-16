(ns kaiuri.clj-yahoo-finance.utils
  (:require
   [camel-snake-kebab.core :as csk]
   [clojure.instant :as time]
   [clojure.spec.alpha :as spec]
   [clojure.string :as string]
   [kaiuri.clj-yahoo-finance.specs])
  (:import [java.util TimeZone
            GregorianCalendar]))

(set! *warn-on-reflection* true)

(defn- gregorian->unix
  [date]
  {:pre [(spec/valid? :date/format date)]}
  (let [^TimeZone timezone (TimeZone/getTimeZone "America/New_York")
        ^GregorianCalendar calendar (time/read-instant-calendar date)]
    (doto calendar
      (.setTimeZone timezone))
    (quot (.getTimeInMillis calendar) 1000)))

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

(defn- index-of
  [^clojure.lang.PersistentVector v element & {:keys [offset] :or {offset 0}}]
  (+ offset (.indexOf v element)))

(defn period->url
  [^clojure.lang.PersistentVector period]
  {:pre [(spec/valid? :query/period period)]}
  (->> period
       (map #(str "period" (index-of period % :offset 1) \= (gregorian->unix %)))
       (string/join \&)))
