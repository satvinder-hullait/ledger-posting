package com.zing.ledger.port.kafka;

import com.zing.ledger.service.LedgerService;
import com.zing.ledger.service.domain.TransactionMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionConsumer {

    private LedgerService ledgerService;

    @KafkaListener(topics = "transaction-topic", groupId = "group_id")
    public void consume(TransactionMessage message) {
        // Wouldn't log possibly sensitive data in real life
        log.info("Received Transaction: " + message);
        ledgerService.writeLedgerEntry(message);
    }
}
