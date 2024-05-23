package com.zing.ledger.port.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionConsumer {

    @KafkaListener(topics = "transaction-topic", groupId = "group_id")
    public void consume(TransactionMessage message) {
        log.info("Received Transaction: " + message);
    }

}
