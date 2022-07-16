(ns kaiuri.clj-yahoo-finance.specs
  (:require [clojure.spec.alpha :as spec]))

(defn time?
  [s]
  (and (string? s)
       (re-matches #"^\d{4}(?:\D\d{2}){2}" s)))

(spec/def :date/format
  (spec/and string? #(re-matches #"^\d{4}(?:\D\d{2}){2}" %)))

(spec/def :query/period
  (spec/coll-of :date/format :kind vector? :count 2 :distinct true))

(spec/def :query/stock string?)

(spec/def :query/frequency
  #(re-matches #"d|w|m" (str %)))

(spec/def :query/extension
  (spec/and keyword? #(re-matches #"clojure|raw" (name %))))

(spec/def :query/query
  (spec/keys :req-un [:query/stock :query/period
                      :query/frequency :query/extension]))
