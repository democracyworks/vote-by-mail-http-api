(ns vote-by-mail-http-api.channels
  (:require [clojure.core.async :as async]))


(defonce ok-requests (async/chan))
(defonce ok-responses (async/chan))

(defonce voter-apply (async/chan 1000))

(defn close-all! []
  (doseq [c [ok-requests ok-responses]]
    (async/close! c)))
