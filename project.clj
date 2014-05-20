(defproject clj-pipes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.303.0-886421-alpha"]
                 [org.clojure/core.typed "0.2.48"]]
  :profiles {:dev {
                   :dependencies [[midje "1.6.0"]]
                   :plugins      [[lein-midje "3.1.1"]
                                  [lein-typed "0.3.4"]]}}
  :core.typed {:check [clj-pipes.core]})
