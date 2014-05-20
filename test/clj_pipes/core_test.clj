(ns clj-pipes.core-test
  (:require [clj-pipes.core :as pipes])
  (:use [midje.sweet]))

(defn producing-fn
  "a test function that will produce a collection"
  []
  (println "producing")
  [1 2 3 "4" "5" 123543])

(defn pipe-fn
  "a test pipe fn"
  [a]
  ;;(println "piping")
  ;;(println a)
  a)

(defn consumer-fn
  "a test consumer"
  [a]
  (println a))

(facts "producer"
  (fact "producing-fn gets called"
    ((pipes/create-producer producing-fn)) => irrelevant
    (provided
      (producing-fn) => ["1" 2 "3"] :times 1)))

(def prod (pipes/create-producer producing-fn))
(def pipe (pipes/create-pipe pipe-fn))
(def consumer (pipes/create-consumer consumer-fn))


(fact "complete process"
  (-> (prod)
        pipe
        consumer
        ) => nil)
