# link-service

Dockerized Clojure microservice for supplying image links from a curated collection.

## Why is this cool?



## Prerequisites

* Supplies a map containing a link to a random image.
* Simple API
* When provided the correct token, allows updating of link collection.
* Periodically attempts to access the curated links and removes dead ones.

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Useage

To start a web server for the application, run:

lein ring server

To access the contents returned from random, you can parse the body.

## Motivation

By providing independent microservices there is a better separation of concerns. Should something break within one service, the others should still operate normally.

This project was created to better provide random links of random things to random people. It was used to better understand certain libraries such as Ring, Compojure, and Korma. A secondary goal was to become familiar with Docker. Finally, it was made to support a Slack bot's need for quality cat gifs acquired throughout the Internet.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
