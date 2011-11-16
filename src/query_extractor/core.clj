(ns query-extractor.core
  (:require [clojure.string :as s])
  (:import java.net.URI))

(def engine-re
  (atom {#".*\.?(google)\.[^/]+" :google 
         #".*\.?(yahoo)\.[^/]+" :yahoo 
         #".*\.?(bing)\.[^/]+" :bing
         #".*\.?(yandex).[^/]+" :yandex
         #".*\.?(ask).[^/]+" :ask
         #".*\.?(search).[^/]+" :search
         #".*\.?(baidu).[^/]+" :baidu
         #".*\.?(aol).[^/]+" :aol}))

(defn params->map [params]
  (->> (s/split params #"&") 
     (map #(s/split % #"="))
     (map (fn [[k v]] [(keyword k) v])) 
     (into {})))

(defn extract-query [^URI ref-uri query-key]
  (let [params (.getQuery ref-uri)
        params-map (when-not (nil? params)
                     (params->map params))]
    (when-not (nil? (query-key params-map))
      (s/replace (query-key params-map) #"\+"
                  " "))))

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

(defmethod handle-engine :yandex
  [uri] (extract-query uri :text))

(defmethod handle-engine :ask
  [uri] (extract-query uri :q))

(defmethod handle-engine :search
  [uri] (extract-query uri :q))

(defmethod handle-engine :baidu
  [uri] (extract-query uri :wd))

(defmethod handle-engine :aol
  [uri] (extract-query uri :q))

(defmethod handle-engine nil
  [uri] nil)

(defn extract [referrer]
  (handle-engine (URI. referrer)))

