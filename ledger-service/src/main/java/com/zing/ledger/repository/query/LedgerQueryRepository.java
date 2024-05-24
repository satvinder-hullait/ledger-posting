package com.zing.ledger.repository.query;

import com.zing.ledger.service.domain.TransactionMessage;
import org.springframework.data.domain.Page;

public interface LedgerQueryRepository {
    Page<TransactionMessage> getTransactionsForAccount(String accountId);
}
