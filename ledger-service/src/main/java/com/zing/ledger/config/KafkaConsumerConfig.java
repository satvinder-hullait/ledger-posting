package com.zing.ledger.config;

import com.zing.ledger.service.domain.TransactionMessage;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, TransactionMessage> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class.getName());
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class.getName());

        props.put(
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
                JsonDeserializer.class.getName());
        props.put(
                ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS,
                StringDeserializer.class.getName());

        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, TransactionMessage.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionMessage>
            kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        //        Sets acknowledgement mode to Record to ensure it acks each message sent,
        //        also setting Kafka into After-Processing mode which means if a db write fails for
        // instance,
        //        the message is not consumed and the offset doesn't move.
        //        Would need to be guided on what the desired behaviour would be in real life,
        //        do we want to retry and then put it into a dead letter queue if it fails x times??
        factory.getContainerProperties().setAckMode(AckMode.RECORD);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
