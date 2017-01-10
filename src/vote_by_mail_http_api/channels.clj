(ns vote-by-mail-http-api.channels
  (:require [clojure.core.async :as async]))

(defonce voter-apply (async/chan 1000))

(defn close-all! []
  (doseq [c [voter-apply]]
    (async/close! c)))
