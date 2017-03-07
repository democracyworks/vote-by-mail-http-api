FROM quay.io/democracyworks/clojure-yourkit:lein-2.7.1

RUN mkdir -p /usr/src/vote-by-mail-http-api
WORKDIR /usr/src/vote-by-mail-http-api

COPY project.clj /usr/src/vote-by-mail-http-api/

ARG env=production

RUN lein with-profile $env deps

COPY . /usr/src/vote-by-mail-http-api

RUN lein with-profiles $env,test test
RUN lein with-profile $env uberjar

CMD java ${JVM_OPTS:--XX:+UseG1GC} \
    -javaagent:resources/jars/com.newrelic.agent.java/newrelic-agent.jar \
    $YOURKIT_AGENT_OPTION \
    -jar target/vote-by-mail-http-api.jar
