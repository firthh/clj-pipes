(ns clj-pipes.core
  (:require ;;[clojure.core.typed :as t :refer [inst check-ns doseq> dotimes> ann-form Seqable]]
            ;;[clojure.core.typed.async :as ta :refer [chan> go> Chan sliding-buffer> dropping-buffer>]]
            [clojure.core.async :as async :refer [chan >! <! go close!]]
            [clojure.core.async.lab :refer [spool]]))

;;(t/typed-deps clojure.core.typed.async)
;;(t/ann create-producer [(Fn [ -> Any]) -> (Fn [ -> (Chan Any)])])


(defn create-producer
  "Takes a function and turns it into a producer"
  [prod-fn]
  (fn []
    (let [data (prod-fn)]
      (spool data))))

(defn create-pipe
  "Takes a function turns it into a pipe.
The supplied function must take a single value and return a single value"
  [pipe-fn]
  (fn [channel]
    (let [c (chan)]
      (async/go (loop []
                  (if-let [v (<! channel)]
                    (do (>! c (pipe-fn v))
                        (recur))))
                (close! c))
      c)))


(defn create-consumer
  "Takes a function and turns in into a consumer"
  [consuming-fn]
  (fn [channel]
    (async/go-loop []
      (if-let [v (<! channel)]
        (do (consuming-fn v)
            (recur))))
    nil))
