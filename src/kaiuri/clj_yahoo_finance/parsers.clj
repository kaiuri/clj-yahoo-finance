(ns kaiuri.clj-yahoo-finance.parsers
  (:require [clojure.string :as string]
            [camel-snake-kebab.core :refer [->snake_case ->camelCase ->kebab-case]]
            [clj-yaml.core :as yaml]
            [cheshire.core :as json]))

(defn csv->clj
  "PRIVATE: Subject to changes.
   Parses CSV string into vector of dicts.
    - <s>: CSV string.
    - [del]: Delimiter, defaults to `,`
  "
  [s & {:keys [del] :or {del #","}}]
  (let [csv (string/split-lines s)
        head (map #(keyword (->kebab-case %)) (string/split (first csv) del))
        body (map #(string/split % del) (rest csv))]
    (mapv #(zipmap head %) body)))

(defn csv->json
  "PRIVATE: Subject to changes.
   Parses CSV string into json string.
    - <s>: CSV string.
    - [del]: Delimiter, defaults to `,`
  "
  [s & {:keys [del] :or {del #","}}]
  (let [csv (string/split-lines s)
        head (map #(keyword (->camelCase %)) (string/split (first csv) del))
        body (map #(string/split % del) (rest csv))]
    (json/generate-string (mapv #(zipmap head %) body))))

(defn csv->yaml
  "PRIVATE: Subject to changes.
   Parses CSV string into yaml string.
    - <s>: CSV string.
    - [del]: Delimiter, defaults to `,`
  "
  [s & {:keys [del] :or {del #","}}]
  (let [csv (string/split-lines s)
        head (map #(keyword (->snake_case %)) (string/split (first csv) del))
        body (map #(string/split % del) (rest csv))]
    (yaml/generate-string (mapv #(zipmap head %) body))))
