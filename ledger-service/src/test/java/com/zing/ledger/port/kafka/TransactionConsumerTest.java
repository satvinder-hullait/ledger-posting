package com.zing.ledger.port.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import com.zing.ledger.BaseKafkaIntegrationTestClass;
import com.zing.ledger.config.KafkaConsumerConfig;
import com.zing.ledger.service.LedgerService;
import com.zing.ledger.service.domain.AccountType;
import com.zing.ledger.service.domain.TransactionCurrency;
import com.zing.ledger.service.domain.TransactionMessage;
import com.zing.ledger.service.domain.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@SpringBootTest(classes = {KafkaConsumerConfig.class, TransactionConsumer.class})
@ExtendWith(MockitoExtension.class)
public class TransactionConsumerTest extends BaseKafkaIntegrationTestClass {

    private static KafkaTemplate<String, TransactionMessage> kafkaTemplate;

    @Autowired private TransactionConsumer consumer;

    @MockBean private LedgerService ledgerService;

    @BeforeEach
    public void setUp() {

        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<String, TransactionMessage> producerFactory =
                new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @Test
    public void testConsumesTransactionMessageCorrectly() {
        TransactionMessage message =
                new TransactionMessage(
                        "12345",
                        AccountType.CURRENT,
                        new BigDecimal("100.00"),
                        TransactionType.DEBIT,
                        TransactionCurrency.GBP,
                        Instant.now());
        doNothing().when(ledgerService).writeLedgerEntry(any());
        kafkaTemplate.send("transaction-topic", message);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        verify(ledgerService).writeLedgerEntry(message);
    }
}
