package com.zing.ledger.port.api;

import com.zing.ledger.service.LedgerService;
import com.zing.ledger.service.domain.TransactionMessage;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
public class LedgerApi {

    private LedgerService ledgerService;

    @PostMapping(
            path = "/account/{accountId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void walletTransaction(@RequestBody TransactionMessage transactionMessage) {
        final String accountId = transactionMessage.accountId();
        log.info("Received request to get transactions for account '{}'", accountId);
        ledgerService.writeLedgerEntry(transactionMessage);
    }

    @GetMapping(path = "/account/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Page<TransactionMessage> getTransactions(
            @PathVariable String accountId, @RequestParam(name = "timestamp") Instant timestamp) {
        log.info(
                "Received request to get transactions for account '{}' up to timestamp '{}'",
                accountId,
                timestamp);

        return ledgerService.getTransactionsForAccountUpToTimestamp(accountId, timestamp);
    }
}
