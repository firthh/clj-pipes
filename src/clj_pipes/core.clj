(ns clj-pipes.core
  (:require ;;[clojure.core.typed :as t :refer [inst check-ns doseq> dotimes> ann-form Seqable]]
            ;;[clojure.core.typed.async :as ta :refer [chan> go> Chan sliding-buffer> dropping-buffer>]]
            [clojure.core.async :as async :refer [chan >! <! go]]))

;;(t/typed-deps clojure.core.typed.async)
;;(t/ann create-producer [(Fn [ -> Any]) -> (Fn [ -> (Chan Any)])])

(defn spool
  "Take a sequence and puts each value on a channel and returns the channel.
   If no channel is provided, an unbuffered channel is created. If the
   sequence ends, the channel is closed."
  ([s c]
     (async/go
      (loop [[f & r] s]
        (if f
          (do
            (async/>! c f)
            (recur r))
          (async/close! c))))
     c)
  ([s]
     (spool s (async/chan))))


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
    (let [c (async/chan)]
      (async/go (loop []
                  (if-let [v (async/<! channel)]
                    (do (async/>! c (pipe-fn v))
                        (recur))))
                (async/close! c))
      c)))


(defn create-consumer
  "Takes a function and turns in into a consumer"
  [consuming-fn]
  (fn [channel]
    (async/go-loop []
      (if-let [v (async/<! channel)]
        (do (consuming-fn v)
            (recur))))
    nil))
