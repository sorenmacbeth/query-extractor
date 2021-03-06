(ns query-extractor.core
  (:require [clojure.string :as s])
  (:import java.net.URI))

(defmacro ^:private def-many-methods
  "Create multiple multimethods with different
   dispatch values but the same implementation"
  [name dispatch-vals args & body]
  (let [methods (for [dval dispatch-vals]
                 `(defmethod ~name ~dval ~args
                    ~@body))]
  `(do ~@methods)))

(defn- params->map [params]
  (into {}
    (for [p (s/split params #"&")
          :let [[k v] (s/split p #"=")]]
      [(keyword k) v] )))

(defn- extract-query [^URI ref-uri query-key]
  (let [params (.getQuery ref-uri)
        params-map (when params (params->map params))]
    (when-let [query-val (query-key params-map)]
      (s/replace query-val #"\+" " "))))

(defn- query-type [^URI ref-uri]
  (when-let [engine (.getHost ref-uri)]
    (cond (re-seq #".*\.?(google)\.[^/]+" engine) :google
          (re-seq #".*\.?(yahoo)\.[^/]+" engine) :yahoo
          (re-seq #".*\.?(bing)\.[^/]+" engine) :bing
          (re-seq #".*\.?(yandex).[^/]+" engine) :yandex
          (re-seq #".*\.?(ask).[^/]+" engine) :ask
          (re-seq #".*\.?(mywebsearch).[^/]+" engine) :mywebsearch
          ;; NB this will match on any domain's 'search' subdomain
          (re-seq #".*\.?(search).[^/]+" engine) :search
          (re-seq #".*\.?(baidu).[^/]+" engine) :baidu
          (re-seq #".*\.?(aol).[^/]+" engine) :aol
          (re-seq #".*\.?(webcrawler).[^/]+" engine) :webcrawler
          (re-seq #".*\.?(mysearchresults).[^/]+" engine) :mysearchresults
          (re-seq #".*\.?(inbox).[^/]+" engine) :inbox
          (re-seq #".*\.?(dogpile).[^/]+" engine) :dogpile)))

(defmulti ^:private handle-engine query-type)

(def-many-methods handle-engine
  [:google :bing :ask :search :aol :verizon :webcrawler :mysearchresults :inbox :dogpile] [uri]
  (extract-query uri :q))

(defmethod handle-engine :yahoo [uri]
  (extract-query uri :p))

(defmethod handle-engine :yandex [uri]
  (extract-query uri :text))

(defmethod handle-engine :baidu [uri]
  (extract-query uri :wd))

(defmethod handle-engine :mywebsearch [uri]
  (extract-query uri :searchfor))

(defmethod handle-engine nil [uri]
  nil)

(defn extract [referrer]
  (when-let [uri (try (URI. referrer) (catch Exception e nil))]
    (handle-engine uri)))
