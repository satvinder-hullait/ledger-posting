package com.zing.ledger.service;

import com.zing.ledger.adapter.command.LedgerWriteRepository;
import com.zing.ledger.adapter.query.LedgerQueryRepository;
import com.zing.ledger.service.domain.TransactionMessage;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class LedgerServiceImpl implements LedgerService {

    private LedgerWriteRepository ledgerWriteRepository;
    private LedgerQueryRepository ledgerQueryRepository;

    @Override
    public void writeLedgerEntry(TransactionMessage transactionMessage) {
        ledgerWriteRepository.saveTransaction(transactionMessage);
    }

    @Override
    public Page<TransactionMessage> getTransactionsForAccountUpToTimestamp(
            String accountId, Instant timestamp) {
        return ledgerQueryRepository.getTransactionsForAccount(accountId, timestamp);
    }
}
