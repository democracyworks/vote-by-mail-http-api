FROM clojure:lein-2.7.1-alpine

RUN mkdir -p /usr/src/vote-by-mail-http-api
WORKDIR /usr/src/vote-by-mail-http-api

COPY project.clj /usr/src/vote-by-mail-http-api/

ARG env=production

RUN lein with-profile $env deps

COPY . /usr/src/vote-by-mail-http-api

RUN lein with-profiles $env,test test
RUN lein with-profile $env uberjar

CMD ["java", "-XX:+UseG1GC", "-jar", "target/vote-by-mail-http-api.jar"]
