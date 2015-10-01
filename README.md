# link-service [![Build Status](https://semaphoreci.com/api/v1/projects/f7bf986f-77d2-496a-9356-695a30319e96/552518/badge.svg)](https://semaphoreci.com/sbauer322/link-service)

A microservice, written in Clojure, for providing a random link from a curated collection. Technically, it does not have to be a link, just a short string (i.e., less than 256 characters). Access is in the form of HTML-based JSON and responses are returned in the same manner.

## Main Features

* Simple API - JSON response containing a "message" and "success?" field.
* Allows updating of link collection by adding or deleting links.

## Why is this cool?

It is perfect for those with a need to provide a random link on demand and via a service. The links can be easily tailored to your context and prefered content. For example, by implementing the API, the bot idling in Slack could provide a link or even update the collection of links straight from the chat.

## How is this different that what is already around?

We haven't quite found anything like link-service, but it is reasonable to expect something similar does exist. An early inspiration was [pugme][] for [Hubot][].

[pugme]: http://pugme.herokuapp.com/random
[hubot]: https://github.com/hubot-scripts/hubot-pugme

If you know of a similar service or project then let us know and we'll add it here!

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running link-service

There are a couple of different ways to use this service.

### Uberjar

Simple uberjar that is ready to deploy. All you need to do is provide the properties on launch. All properties except for `-Dtokens` have defaults and can be omitted. For instance:

```
java -Ddb.user=sa -Ddb.password=password1 -Dtokens=d89b060e,8sd1a2xb -jar link-service-0.1.0-SNAPSHOT-standalone.jar
```

#### Properties

| Property       | Example       | Optional  | Default       |
| -------------- |:------------- |:---------:|:--------------|
| -Ddb.user      | sa            |     y     |sa             |
| -Ddb.password  | password1     |     y     |               |
| -Dtokens       | d89b060e      |     n     |<i>required</i>|

Note that `-Dtokens` should have a comma in between each token with no spaces.

### Repl

Clone the project and in the project's directory run `lein repl` from the command line followed by `(-main)`. Make sure you add a `profiles.clj` file as described below in Development Notes.

## API

In order to interact with the service you will want to implement the API outlined below. Alternatively, you could use a browser plugin such as [RESTED][].

[rested]: https://github.com/esphen/RESTED

There are three endpoints `/random`, `/add`, and `/delete`. All communication to and from is through [JSON][]. If you happen to use Clojure then check out the excellent [Cheshire][] for JSON encoding/decoding.

[json]: http://json.org/
[cheshire]: https://github.com/dakrone/cheshire

To send a request for a link, use a GET request against `/random`. Make sure the Content-Type of your header is correct. It should be `application/json`.

To access the content returned from `/random`, you can parse the body. It should just be a JSON map like the following example:

```json
{
"link": "https://github.com/ring-clojure/ring"
}
```

To add content, you can use `/add` and send a POST with a JSON body as follows:

```json
{
"link": "https://github.com/ring-clojure/ring",
"token": "d89b060e"
}
```

Deleting content works the same way as `/add` but with the endpoint `/delete`.

Sending an invalid token will result in no changes and a message is returned stating failure.

The maximum link length is 255 characters.

## Motivation

By providing independent microservices there is a better separation of concerns. Should something break within one service, the others should still operate normally.

This project was created to better provide random links of random things to random people. It was used to better understand certain libraries such as Ring, Compojure, and Korma. A secondary goal was to become familiar with Docker. More importantly, it was made to support a certain Slack bot's need for quality cat gifs acquired throughout the Internet.

## Development Notes

To run tests: `lein test`

To run documentation generation: `lein doc`

To run the repl: `lein repl`

To package the uberjar: `lein uberjar`

### Profiles.clj

Be sure to create and populate a file named `profiles.clj` at the root of the project directory. This file should contain various config info, such as the database user and password as well as tokens. You'll want to mimic the following, but with better passwords and tokens:

```
{:dev-overrides  {:env {:db-user        "sa"
                        :db-password    "password1"
                        :tokens         ["aaaaaaaa"]}}
 :test-overrides {:env {:db-user        "sa"
                        :db-password    "password1"
                        :tokens         ["bbbbbbbb"]}}
 :uberjar-overrides {:env {:db-user     "sa"
                           :db-password    "password1"
                           :tokens         ["cccccccc"]}}}
```

### Unimplmented Features

* Tagging links so results can be returned based on tags.
* Dead link checking - periodically attempts to access the curated links and removes dead ones.
* Runs in Docker.

## Parting Notes

If you plan to use this project (or judge my skills) please keep in mind it was developed as a tool for learning in my spare time and may have bugs, major design flaws, or other issues. That said, feel free to submit issues, pull requests, or questions. I would be happy to assist.

## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.