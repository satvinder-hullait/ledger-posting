package com.zing.ledger.performance;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Instant;

public class LedgerApiSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .contentTypeHeader("application/json")
        .userAgentHeader("Gatling");

    ChainBuilder postTransactions =
        exec(
            http("Post Transaction")
                .post("/account/test-account") // Replace with actual accountId if necessary
                .body(ElFileBody("transaction.json")).asJson()
                .check(status().is(202))
        ).pause(1);

    ChainBuilder getTransactions = repeat(100).on(exec(
        http("Get Transactions")
            .get("/account/test-account")
            .queryParam("timestamp", Instant.now().toString())
            .check(status().is(200))
    ).pause(1));

    ScenarioBuilder getTransactionScenario = scenario("LedgerApiSimulation")
        .exec(getTransactions);

    @Override
    public void before() {
        exec(postTransactions);
    }

    {
        int millisecondThresholdForResponseTime = 2000;
        setUp(

            getTransactionScenario.injectOpen(
                atOnceUsers(1),
                rampUsers(10).during(10)
            ).protocols(httpProtocol)
        ).assertions(
            global().responseTime().max().lt(millisecondThresholdForResponseTime),
            global().successfulRequests().percent().is(100.0));
    }

}
