(ns query-extractor.test.core
  (:use query-extractor.core
        midje.sweet))

(tabular "urls with a query to extract"
  (fact (extract url) => "a test search")
  
    url
    "http://www.bing.com/search?q=a+test+search&go=&qs=n&sk=&sc=1-13&form=QBLH"
    "http://www.google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
    "http://video.google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
    "http://images.google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
    "http://google.com/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
    "http://www.google.de/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
    "http://www.google.co.uk/?q=a+test+search&hl=en&biw=1405&bih=783&fp=1&bav=on.2,or.r_gc.r_pw.&cad=b"
    "http://search.yahoo.com/search;_ylt=ArsdL6su1Uh4YCFQvqwY8tCbvZx4?p=a+test+search&toggle=1&cop=mss&ei=UTF-8&fr=yfp-t-701"
    "http://yandex.com/yandsearch?text=a+test+search&lr=84"
    "http://www.ask.com/web?q=a+test+search&search=&qsrc=0&o=0&l=dir"
    "http://www.search.com/search?q=a+test+search"
    "http://www.baidu.com/s?wd=a+test+search&rsv_bp=0&inputT=5124"
    "http://search.aol.com/aol/search?enabled_terms=&s_it=comsearch50&q=a+test+search"  )

(tabular "urls with no query to extract" 
  (fact (extract url) => nil)

    url
    ""
    "http://no-params.com"
    "http://www.google.com/?x=foo")

