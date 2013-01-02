(defproject query-extractor "0.0.9"
  :description "A library for extracting search query strings for HTTP referrer strings"
  :url "http://github.com/sorenmacbeth/query-extractor"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.4.0"]]
  :profiles {:dev
             {:dependencies [[midje "1.4.0"]]
              :plugins [[lein-midje "2.0.3"]]}})
