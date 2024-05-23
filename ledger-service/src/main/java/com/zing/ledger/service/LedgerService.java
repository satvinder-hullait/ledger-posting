package com.zing.ledger.service;

import com.zing.ledger.service.domain.TransactionMessage;

public interface LedgerService {

    void writeLedgerEntry(TransactionMessage transactionMessage);
}
