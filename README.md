# query-extractor

A very small library for extracting a search query string given a
referrer address. It should work for Yahoo, Google, and Bing
searches. If it doesn't please let me know so the library can improve.

## Usage

for leiningen: 

`[query-extractor "0.0.1-SNAPSHOT"]`

An example REPL session:

    => (use 'query-extractor.core)
    => (extract "http://www.google.com/search?q=just+an+example")
    =>"just an example"

## License

Copyright (C) 2011 Soren Macbeth

Distributed under the Eclipse Public License, the same as Clojure.
