(defproject vote-by-mail-http-api "0.1.0-SNAPSHOT"
  :description "HTTP API gateway for vote by mail services"
  :url "https://github.com/democracyworks/vote-by-mail-http-api"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [turbovote.resource-config "0.2.0"]
                 [com.novemberain/langohr "3.5.0"]
                 [prismatic/schema "1.0.4"]
                 [ch.qos.logback/logback-classic "1.1.3"]

                 ;; core.async has to come before pedestal or kehaar.wire-up will
                 ;; not compile. Something to do with the try-catch in
                 ;; kehaar.core/go-handler. (This may not be true anymore in
                 ;; core.async 0.2.x; need to test.)
                 [org.clojure/core.async "0.2.374"]
                 [democracyworks/kehaar "0.10.3"]

                 [io.pedestal/pedestal.service "0.4.1"]
                 [io.pedestal/pedestal.service-tools "0.4.1"]
                 [democracyworks/pedestal-toolbox "0.7.0"]
                 [org.immutant/web "2.1.2"]
                 [io.pedestal/pedestal.immutant "0.4.1"]
                 [org.immutant/core "2.1.2"]
                 [democracyworks/bifrost "0.1.5"]]
  :plugins [[lein-immutant "2.1.0"]
            [com.carouselapps/jar-copier "0.3.0"]]
  :java-agents [[com.newrelic.agent.java/newrelic-agent "3.35.1"]]
  :jar-copier {:java-agents true
               :destination "resources/jars"}
  :prep-tasks ["javac" "compile" "jar-copier"]
  :main ^:skip-aot vote-by-mail-http-api.server
  :uberjar-name "vote-by-mail-http-api.jar"
  :profiles {:uberjar {:aot :all}
             :dev {:resource-paths ["dev-resources"]}
             :test {:dependencies [[clj-http "2.0.0"]]
                    :jvm-opts ["-Dlog-level=OFF"]}})
