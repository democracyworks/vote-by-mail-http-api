# vote-by-mail-http-api

HTTP API gateway for vote-by-mail services.

## Configuration

* ALLOWED_ORIGINS
    * This env var controls the cross-origin resource sharing (CORS) settings.
    * It should be set to one of the following:
        * `[".*"]` to allow requests from any origin
        * an EDN seq of allowed origin strings
        * an EDN map containing the following keys and values
            * :allowed-origins - sequence of strings
            * :creds - true or false, indicates whether client is allowed to send credentials
            * :max-age - a long, indicates the number of seconds a client should cache the response from a preflight request
            * :methods - a string, indicates the accepted HTTP methods.  Defaults to "GET, POST, PUT, DELETE, HEAD, PATCH, OPTIONS"
    * For example: `ALLOWED_ORIGINS=["http://foo.example.com" "http://bar.example.com"]`

## Usage

To initiate the vote-by-mail application process, send a POST request to the
`/applications` endpoint with a body like this:

```
{:form :ca_absentee, :voter {:first-name "Wes", :last-name "Morgan"}}
```

If successful, the response will look like this:

```
{:form-uri "https://foo.bar/Wes_Morgan_ca_absentee.pdf"}
```

## Running

### With docker-compose

Build it:

```
> docker-compose build
```

Run it:

```
> docker-compose up
```

TODO: If any more env vars are needed, add them to docker-compose command above.

### Running in CoreOS

There is a vote-by-mail-http-api@.service.template file provided in the repo. Look
it over and make any desired customizations before deploying. The
DOCKER_REPO, IMAGE_TAG, and CONTAINER values will all be set by the
build script.

The `script/build` and `script/deploy` scripts are designed to
automate building and deploying to CoreOS.

1. Run `script/build`.
1. Note the resulting image name and push it if needed.
1. Set your FLEETCTL_TUNNEL env var to a node of the CoreOS cluster
   you want to deploy to.
1. Make sure rabbitmq service is running.
1. Run `script/deploy`.

## License

Copyright © 2016 Democracy Works, Inc.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
