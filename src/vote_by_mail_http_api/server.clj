(ns vote-by-mail-http-api.server
    (:require [vote-by-mail-http-api.service :as service]
              [io.pedestal.http :as http]
              [vote-by-mail-http-api.channels :as channels]
              [vote-by-mail-http-api.queue :as queue]
              [turbovote.resource-config :refer [config]]
              [clojure.tools.logging :as log]
              [immutant.util :as immutant])
  (:gen-class))

(defn shutdown [rabbit-resources]
  (channels/close-all!)
  (queue/close-all! rabbit-resources))

(defn start-http-server [& [options]]
  (-> (service/service)
      (merge options)
      http/create-server
      http/start))

(defn -main [& args]
  (let [rabbit-resources (queue/initialize)]
    (start-http-server (config [:server]))
    (immutant/at-exit (partial shutdown rabbit-resources))))
