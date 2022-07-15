# yahoo-finance

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.kaiuri/clj-yahoo-finance.svg)](https://clojars.org/net.clojars.kaiuri/clj-yahoo-finance)

Clojure library for fetching [_yahoo!finance_](finance.yahoo.com) historical data. No HTML parsing.

## Usage

`yahoo-finance` provides a single function `get-historical` which takes dict:

- `stock`: Stock's name (string) as displayed in `finance.yahoo.com`.
- `period`: PersistentVector tuple of date strings, each in the format `YYYY-MM-dd`.
- `[frequency]`: String, one of `w`(week), `d`(day) or `m`(month).
- `[extension]`: One of `:clojure`, `:raw`, defaults to `:clojure`.

And returns the data.

```clojure
(require '[kaiuri.clj-yahoo-finance.core :as yf])
(->> (yf/get-historical {:stock "^RUT", :period ["2021-10-10" "2022-01-01"]
                         :frequency "d" :extension :clojure})
     (println))

#_=> [{:date 2021-10-11, :open 2233.000000, :high 2251.649902, :low 2220.639893, :close 2220.639893, :adj-close 2220.639893, :volume 2580000000}, ...]
```

## TODO

- [ ] Add more output formats.
