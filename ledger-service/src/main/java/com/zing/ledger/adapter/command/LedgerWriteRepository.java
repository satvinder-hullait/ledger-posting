package com.zing.ledger.adapter.command;

import com.zing.ledger.service.domain.TransactionMessage;

public interface LedgerWriteRepository {

    void saveTransaction(TransactionMessage transactionMessage);
}
