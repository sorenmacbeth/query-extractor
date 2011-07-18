(ns query-extractor.test.core
  (:use [query-extractor.core])
  (:use [clojure.test]))


(deftest test-extract
  (let [want "a test search"
        tests ["http://www.bing.com/search?q=a+test+search&go=&qs=n&sk=&sc=1-13&form=QBLH"
               "http://www.google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
               "http://video.google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
               "http://images.google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
               "http://google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
               "http://www.google.de/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
               "http://www.google.co.uk/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
               "http://search.yahoo.com/search;_ylt=ArsdL6su1Uh4YCFQvqwY8tCbvZx4?p=a+test+search&toggle=1&cop=mss&ei=UTF-8&fr=yfp-t-701"]]
    (doall (map #(is (= want (extract %))) tests))))

(deftest test-nils
  (let [want nil
        tests [""
               "http://no-params.com"]]
    (doall (map #(is (= want (extract %))) tests))))

