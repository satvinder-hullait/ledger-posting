package com.zing.ledger.port.kafka;

import com.zing.ledger.BaseKafkaIntegrationTestClass;
import com.zing.ledger.config.KafkaConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.DynamicPropertyRegistry;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {KafkaConsumerConfig.class, TransactionConsumer.class})
public class TransactionConsumerTest extends BaseKafkaIntegrationTestClass {

    private static KafkaTemplate<String, TransactionMessage> kafkaTemplate;

    @Autowired
    private TransactionConsumer consumer;

    @BeforeEach
    public void setUp() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        ProducerFactory<String, TransactionMessage> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
        kafkaTemplate = new KafkaTemplate<>(producerFactory);
    }

    @Test
    public void testKafkaConsumer() throws InterruptedException {
        TransactionMessage message = new TransactionMessage("12345", AccountType.CURRENT, new BigDecimal("100.00"), TransactionType.DEBIT, Instant.now());
        kafkaTemplate.send("transaction-topic", message);

        // Give some time for the message to be consumed
        Thread.sleep(2000);

        // Validate that the message was consumed
//        assertThat(consumer.getMessages()).contains(message);
    }
}
