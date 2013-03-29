(defproject query-extractor "0.1.0-SNAPSHOT"
  :description "A library for extracting search query strings for HTTP referrer strings"
  :url "http://github.com/sorenmacbeth/query-extractor"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]]
  :profiles {:dev
             {:dependencies [[midje "1.5.0"]]
              :plugins [[lein-midje "3.0.0"]]}})
