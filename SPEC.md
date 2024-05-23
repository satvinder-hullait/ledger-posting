# Assignment:

## Ledger Posting System Design and Implementation

### Key Areas of Assessment
- Microservices Architecture
- CQRS and Event Sourcing Patterns for implementation
- Highly Testable and behaviour driven development
- API design and its ease of use

### Acceptance Criteria
- Integration and Unit Test Cases that are readable and behaviour driven (Must)
- System able to communicate with the client both synchronously and asynchronously
- Swagger Spec for APIs
- Read me if required to pass on any additional information to the reviewer
- Write Gatling based Load Test for one of the APIs (Must)
- Write JUnit based integration test for one of the APIs (Must)-assume that rest of the third party services are not ready hence use mocks/stubs wherever appropriate

**Note:Specify details of all six points (above) in ReadMe for the interview panel to look at implementations.**


### Stack
- Java 17 and above
- Spring Boot, Junit, Mockito, Gatling (Performance Testing)
- Any other components that help the complete implementation like messaging frameworks , testing framework etc

### Principles of Development

We advocate for a progressive approach, prioritizing limited but well-implemented and testable features over a plethora of features lacking in implementation quality and testability. Embracing the MVP (Minimum Viable Product) methodology, we value incremental development, starting with basic functionalities and gradually expanding to more advanced features, akin to the progression from a scooty to a bike, a four-wheeler, and finally a car

### Problem Statement
Create a Java/Spring Boot-based ledger system with a scalable architecture to handle heavy write loads efficiently. Utilize CQRS, Event Sourcing, and microservices principles to ensure modularity and scalability. The system should provide retrievable ledger balances for a given timestamp, cater to API-friendly interactions for asset transfers and account management, and implement comprehensive behavior-driven testing. Additionally, prioritize a user-friendly API design for seamless integration with external systems.

### Submission Notes:
Please get the code pushed to personal github repository with a clear readme file describing key features of the code being developed.

*Optional: Describe about the code via private link of youtube which can be used via link and available within ReadMe file*