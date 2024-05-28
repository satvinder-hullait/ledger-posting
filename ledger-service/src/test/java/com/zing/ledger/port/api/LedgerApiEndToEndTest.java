package com.zing.ledger.port.api;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.zing.ledger.BaseRedisIntegrationTestClass;
import com.zing.ledger.service.domain.AccountType;
import com.zing.ledger.service.domain.TransactionCurrency;
import com.zing.ledger.service.domain.TransactionMessage;
import com.zing.ledger.service.domain.TransactionType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.math.BigDecimal;
import java.time.Instant;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LedgerApiEndToEndTest extends BaseRedisIntegrationTestClass {

    @LocalServerPort private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void testWalletTransaction() {
        String accountId = "1234";
        String transactionId = "1234";

        TransactionMessage transactionMessage =
                new TransactionMessage(
                        transactionId,
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());

        RestAssured.with()
                .contentType(ContentType.JSON)
                .body(transactionMessage)
                .when()
                .post("/account/{accountId}", accountId)
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);
    }

    @Test
    void testGetTransactionsWithTimestamp() {
        String accountId = "1234";
        String transactionId = "1234";
        Instant now = Instant.now();

        TransactionMessage transactionMessage =
                new TransactionMessage(
                        transactionId,
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        now);

        RestAssured.with()
                .contentType(ContentType.JSON)
                .body(transactionMessage)
                .when()
                .post("/account/{accountId}", accountId)
                .then()
                .statusCode(HttpStatus.SC_ACCEPTED);

        Response response =
                given().pathParam("accountId", accountId)
                        .queryParam("timestamp", now.toString())
                        .get("/account/{accountId}");

        response.then().statusCode(HttpStatus.SC_OK);

        String responseBody = response.getBody().asString();
        assertThat(responseBody).contains("\"accountId\":\"" + accountId + "\"");
        assertThat(responseBody).contains("\"transactionId\":\"" + transactionId + "\"");
        assertThat(responseBody).contains("\"amount\":1");
        assertThat(responseBody).contains("\"transactionType\":\"CREDIT\"");

        System.out.println(response.body().prettyPrint());
    }

    @Test
    void testHandleConflict_LedgerWriteRepositoryException() {
        String accountId = "1234";
        String transactionId = "1234";

        TransactionMessage transactionMessage =
                new TransactionMessage(
                        transactionId,
                        accountId,
                        AccountType.CURRENT,
                        BigDecimal.ONE,
                        TransactionType.CREDIT,
                        TransactionCurrency.GBP,
                        Instant.now());

        ValidatableResponse firstMessage =
                RestAssured.with()
                        .contentType(ContentType.JSON)
                        .body(transactionMessage)
                        .when()
                        .post("/account/{accountId}", accountId)
                        .then()
                        .statusCode(HttpStatus.SC_ACCEPTED);

        given().with()
                .contentType(ContentType.JSON)
                .body(transactionMessage)
                .when()
                .post("/account/{accountId}", accountId)
                .then()
                .statusCode(HttpStatus.SC_CONFLICT)
                .body(equalTo("Duplicate transaction for account: " + accountId));
    }

    @Test
    void testInternalServerError_LedgerQueryRepositoryException() {
        String nonExistingAccountId = "nonExistingAccountId";
        given().contentType("application/json")
                .when()
                .get("/account/" + nonExistingAccountId + "?timestamp=2024-05-28T12:00:00Z")
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(equalTo("Account: " + nonExistingAccountId + ", does not exist in ledger"));
    }
}
