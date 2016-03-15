(ns vote-by-mail-http-api.queue
  (:require [clojure.tools.logging :as log]
            [langohr.core :as rmq]
            [kehaar.core :as k]
            [kehaar.wire-up :as wire-up]
            [kehaar.rabbitmq]
            [vote-by-mail-http-api.channels :as channels]
            [vote-by-mail-http-api.handlers :as handlers]
            [turbovote.resource-config :refer [config]]))

(defn initialize []
  (let [max-retries 5
        rabbit-config (config [:rabbitmq :connection])
        connection (kehaar.rabbitmq/connect-with-retries rabbit-config max-retries)]
    (let [incoming-events []
          incoming-services [(wire-up/incoming-service
                              connection
                              "vote-by-mail-http-api.ok"
                              (config [:rabbitmq :queues "vote-by-mail-http-api.ok"])
                              channels/ok-requests
                              channels/ok-responses)]
          external-services [(wire-up/external-service
                              connection "" "vote-by-mail-works.voter.apply"
                              (config [:rabbitmq :queues
                                       "vote-by-mail-works.voter.apply"])
                              (config [:timeouts :voter-apply])
                              channels/voter-apply)]
          outgoing-events []]

      (wire-up/start-responder! channels/ok-requests
                                channels/ok-responses
                                handlers/ok)

      {:connections [connection]
       :channels (vec (concat
                       incoming-events
                       incoming-services
                       external-services
                       outgoing-events))})))

(defn close-resources! [resources]
  (doseq [resource resources]
    (when-not (rmq/closed? resource) (rmq/close resource))))

(defn close-all! [{:keys [connections channels]}]
  (close-resources! channels)
  (close-resources! connections))
