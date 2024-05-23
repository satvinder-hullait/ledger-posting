package com.zing.ledger.service;

import com.zing.ledger.service.domain.TransactionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class LedgerServiceImpl implements LedgerService {
    @Override
    public void writeLedgerEntry(TransactionMessage transactionMessage) {
        log.info("Writing ledger entry");
    }
}
