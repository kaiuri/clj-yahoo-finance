# yahoo-finance

[![Clojars Project](https://img.shields.io/clojars/v/net.clojars.kaiuri/yahoo-finance.svg)](https://clojars.org/net.clojars.kaiuri/yahoo-finance)

Clojure library for fetching [_yahoo!finance_](finance.yahoo.com) historical data. No HTML parsing.

## Usage

`yahoo-finance` provides a single function `get-historical` which takes dict:

- `stock`: Stock's name (string) as displayed in `finance.yahoo.com`.
- `period`: PersistentVector tuple of date strings, each in the format `YYYY-MM-dd`.
- `[frequency]`: String, one of `w`(week), `d`(day) or `m`(month).
- `[extension]`: One of `:clojure`, `:raw`, defaults to `:clojure`.

And returns the data.

```clojure
(require '[kaiuri.yahoo-finance :as yf])
(->> (yf/get-historical {:stock "^RUT", :period ["2021-10-10" "2022-01-01"]
                         :frequency "d" :extension :clojure})
     (println))

#_=> [{:Date 2021-10-11, :Open 2233.000000, :High 2251.649902, :Low 2220.639893, :Close 2220.639893, :Adj Close 2220.639893, :Volume 2580000000}, ...]
```

## TODO

- [ ] Add more output formats.
