(defproject sandbox "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.googlecode.lanterna/lanterna "2.1.7"]]
  :main ^:skip-aot sandbox.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
