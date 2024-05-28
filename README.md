# Ledger Posting System Tech Test


## Acceptance Criteria
- Integration and Unit Test Cases that are readable and behaviour driven (Must)
  - Visible in the ledger-service repository, due to the nature of the assignment, there are largely integration tests with test containers with some end to end tests using rest assured
- System able to communicate with the client both synchronously and asynchronously
  - The services both consumes from a Kafka Topic and is able to receive and return transactions via HTTP, you can see this in the 'ports' module of ledger-service
- Swagger Spec for APIs
  - Spin up the app (instructions in ledger-service readme) and access http://localhost:8080/swagger-ui/index.html
- Read me if required to pass on any additional information to the reviewer
  - In relevant Repos
- Write Gatling based Load Test for one of the APIs (Must)
  - Exists in ledger-performance-tests
- Write JUnit based integration test for one of the APIs (Must)-assume that rest of the third party services are not ready hence use mocks/stubs wherever appropriate
  - Demonstrated in ledger-setvice tests

**Note:Specify details of all six points (above) in ReadMe for the interview panel to look at implementations.**

## Submission Notes

The Ledger Service follows the 'ports and adapters' pattern, inbound (messages and http requests in this case) traffic being ports and outbound (write to a repository or another system) being adapters. You can see in the ports section the system is capable of SYNC and ASYNC receipt of transaction messages. It has a service layer between to orchestrate where things should go

This system would form part of an estate of microservices receiving transactions from a transaction topic which would presumably be written to by a transaction-service, I could also implement an endpoint which accepts a collection of transactions too

I decided to use Kafka and Redis as I understand they form part of your stack, and, they helped demonstrate the use cases. I did not write a producer for kafka but have tested manually producing a message inside of the docker container and verified it is read successfully

I have added spotless as an example of the things I like to implement to enforce a code style in a repository so that can free up pull request reviewers to look at just the code changes not on stylistic points. In addition to this in an actual working environment, would also add more automated tools such as PMD, Findbugs, Sonarqube, Checkmarx, DependencyChecker etc. To ensure we have high quality release candidates
