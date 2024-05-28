# Ledger service

Ledger service for keeping a record of transactions for an account

## Pre-reqs


* Java 21
* Docker
  * run `docker-compose up` in the root of this repository once that has run, then execute the `docker ps` command you shoul dsee Kafka, Kafka Zookeeper and Redis running

## Running this app

To run this app either use your IDE to run or simple run `./gradlew bootRun` whilst the dependent services are running