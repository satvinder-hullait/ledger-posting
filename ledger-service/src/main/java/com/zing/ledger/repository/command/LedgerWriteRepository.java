package com.zing.ledger.repository.command;

import com.zing.ledger.service.domain.TransactionMessage;

public interface LedgerWriteRepository {

    void saveTransaction(TransactionMessage transactionMessage);
}
