# Discography API [![Build Status](https://travis-ci.com/ChrisWilding/discography-api.svg?branch=master)](https://travis-ci.com/ChrisWilding/discography-api)

A GraphQL API for discography.

You can try it out [here](http://cw-discography-api.herokuapp.com/graphql).

## Prerequisites

1. [Java 11](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot)
1. [SBT](https://www.scala-sbt.org/download.html)

## Installation

```sh
$ git clone git@github.com:ChrisWilding/discography-api.git
$ cd discography-api
$ sbt update
```

## Usage

```sh
$ sbt run
```

Project is running at http://localhost:9000/

## Docker

### Prerequisites

1. [Docker](https://hub.docker.com/search/?type=edition&offering=community)

### Usage

```sh
$ docker-compose build
$ docker-compose up
```

Project is running at http://localhost:9000/
