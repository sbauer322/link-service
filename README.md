# link-service

Docker-ized Clojure microservice for supplying links from a curated collection of cat image links.
FIXME

## Prerequisites

* Supplies a map containing a link to a random cat image.
* Simple API
* When provided the correct token, allows updating of link collection.
* Periodically attempts to access the curated links and removes dead ones.
You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.