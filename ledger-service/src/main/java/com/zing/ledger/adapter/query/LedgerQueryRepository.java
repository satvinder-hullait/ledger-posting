package com.zing.ledger.adapter.query;

import com.zing.ledger.service.domain.TransactionMessage;
import java.time.Instant;
import org.springframework.data.domain.Page;

public interface LedgerQueryRepository {
    Page<TransactionMessage> getTransactionsForAccount(String accountId, Instant timestamp);
}
