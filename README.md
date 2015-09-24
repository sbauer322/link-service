# link-service

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
"token": "adsvsx41432ad$sf45a6s"
}
```

Sending an invalid token will result in the link not being added.

## Development Notes

To run tests: `lein test`

To run the repl: `lein repl`

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