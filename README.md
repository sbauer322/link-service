# link-service [![Build Status](https://semaphoreci.com/api/v1/projects/f7bf986f-77d2-496a-9356-695a30319e96/552518/badge.svg)](https://semaphoreci.com/sbauer322/link-service)

Dockerized Clojure microservice for supplying image links from a curated collection.

## Main Features

* Simple API - JSON containing a link to a random image.
* Allows updating of link collection.
* Periodically attempts to access the curated links and removes dead ones.
* Runs in Docker and written in Clojure.

## Why is this cool?



## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Usage

To start a web server for the application, run `lein ring server`.

To access the content returned from /random, you can parse the body. It should just be a JSON map like the following example:

```
{
"link": "https://github.com/ring-clojure/ring"
}
```

To add content, you can use /add and send a POST with a JSON body as follows:

```
{
"link": "https://github.com/ring-clojure/ring",
"token": "d89b060e"
}
```

Deleting works the same way but with the endpoint /delete.

Make sure the Content-Type of your header is correct. It should be `application/json`.

Sending an invalid token will result in the link not being added.

The maximum link length is 255 characters.

To run the uberjar:

```java -Ddb.user=sa -Ddb.password=3335b2f3f27cf3ee91f8797d034843ed6682e4974a9aa5bca6146bd70737887e -Dtokens=d89b060e -jar link-service-0.1.0-SNAPSHOT-standalone.jar```

-Dtokens should have a comma in between each acceptable token and no spaces.


## Development Notes

To run tests: `lein test`

To run documentation generation: `lein doc`

To run the repl: `lein repl`

To run for dev: `lein ring server-headless` port 3000

To package the uberjar: `lein ring uberjar`

Be sure to populate your profiles.clj as it contains various config info, such as the database user and password as well as tokens. You'll want to mimic the following, but with better passwords and tokens:

```
{:dev-overrides  {:env {:db-user        "sa"
:db-password    "password"
:tokens         ["aaaaaaaa"]}}
:test-overrides {:env {:db-user        "sa"
:db-password    "password"
:tokens         ["bbbbbbbb"]}}
:uberjar-overrides {:env {:db-user     "sa"
:db-password    "password"
:tokens         ["cccccccc"]}}}
```

## How is this different that what is already around?

## Motivation

By providing independent microservices there is a better separation of concerns. Should something break within one service, the others should still operate normally.

This project was created to better provide random links of random things to random people. It was used to better understand certain libraries such as Ring, Compojure, and Korma. A secondary goal was to become familiar with Docker. More importantly, it was made to support a certain Slack bot's need for quality cat gifs acquired throughout the Internet.

## Parting Notes

If you plan to use this project (or judge my skills) please keep in mind it was developed as a tool for learning in my spare time and may have bugs, major design flaws, other issues. That said, feel free to submit issues, pull requests, or questions.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
