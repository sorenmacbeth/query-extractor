(ns query-extractor.core
  (:require [url-normalizer.core :as norm])
  (:require [clojure.contrib.str-utils :as su])
  (:import java.net.URI))

(def engine-re
  (atom {#".*\.?(google)\.[^/]+" :google 
         #".*\.?(yahoo)\.[^/]+" :yahoo 
         #".*\.?(bing)\.[^/]+" :bing }))

(defn params->map [params]
  (->> (su/re-split #"&" params) 
     (map #(su/re-split #"=" %))
     (map (fn [[k v]] [(keyword k) v])) 
     (into {})))

(defn extract-query [^URI ref-uri query-key]
  (let [params (.getQuery ref-uri)
        params-map (when-not (nil? params)
                     (params->map params))]
    (when-not (nil? (query-key params-map))
      (su/re-gsub #"\+"
                  " "
                  (query-key params-map)))))

(defn query-type [^URI ref-uri]
  (when-not (nil? (.getHost ref-uri))
    (some (fn [[re t]]
          (let [engine (.getHost ref-uri)]
            (and (not (nil? (re-seq re engine)))
                 t)))
        @engine-re)))

(defmulti handle-engine query-type)

(defmethod handle-engine :google
  [uri] (extract-query uri :q))

(defmethod handle-engine :bing
  [uri] (extract-query uri :q))

(defmethod handle-engine :yahoo
  [uri] (extract-query uri :p))

(defmethod handle-engine nil
  [uri] nil)

(defn extract [referrer]
  (handle-engine (URI. (norm/canonicalize-url referrer))))

