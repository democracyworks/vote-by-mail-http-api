FROM quay.io/democracyworks/didor:latest

RUN mkdir -p /usr/src/vote-by-mail-http-api
WORKDIR /usr/src/vote-by-mail-http-api

COPY project.clj /usr/src/vote-by-mail-http-api/

RUN lein deps

COPY . /usr/src/vote-by-mail-http-api

RUN lein test
RUN lein immutant war --name vote-by-mail-http-api --destination target --nrepl-port=11111 --nrepl-start --nrepl-host=0.0.0.0
