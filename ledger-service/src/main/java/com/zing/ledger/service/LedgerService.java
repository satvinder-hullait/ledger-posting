package com.zing.ledger.service;

import com.zing.ledger.service.domain.TransactionMessage;
import java.time.Instant;
import org.springframework.data.domain.Page;

public interface LedgerService {

    void writeLedgerEntry(TransactionMessage transactionMessage);

    Page<TransactionMessage> getTransactionsForAccountUpToTimestamp(
            String accountId, Instant timestamp);
}
