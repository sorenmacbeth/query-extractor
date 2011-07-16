(ns query-extractor.core
  (:require [url-normalizer.core :as norm])
  (:require [clojure.contrib.str-utils :as su])
  (:import java.net.URI))

(def engine-re {
              :google #".*\.?(google)\.[^/]+"
              :yahoo #".*\.?(yahoo)\.[^/]+"
              :bing #".*\.?(bing)\.[^/]+"})

(def query-key {
                :google :q
                :bing :q
                :yahoo :p})

(defn- params->map [params]
  (->> (su/re-split #"&" params) 
     (map #(su/re-split #"=" %))
     (map (fn [[k v]] [(keyword k) v])) 
     (into {})))

(defn- extract-query [#^URI ref-uri engine]
  (let [params (.getQuery ref-uri)
        params-map (when-not (nil? params)
                     (params->map params))]
    (when-not (nil? params-map)
      (su/re-gsub #"\+"
                  " "
                  ((engine query-key) params-map)))))

(defn- google? [#^URI ref-uri]
  (let [engine (.getHost ref-uri)]
    (not (nil? (re-seq (:google engine-re) engine)))))

(defn- yahoo? [#^URI ref-uri]
  (let [engine (.getHost ref-uri)]
    (not (nil? (re-seq (:yahoo engine-re) engine)))))

(defn- bing? [#^URI ref-uri]
  (let [engine (.getHost ref-uri)]
    (not (nil? (re-seq (:bing engine-re) engine)))))

(defn extract [referrer]
  (let [ref-norm (norm/canonicalize-url referrer)
        ref-uri (URI. ref-norm)]
    (cond
     (google? ref-uri) (extract-query ref-uri :google)
     (yahoo? ref-uri) (extract-query ref-uri :yahoo)
     (bing? ref-uri) (extract-query ref-uri :bing))))

