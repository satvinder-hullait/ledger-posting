package com.zing.ledger;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseKafkaIntegrationTestClass {

    protected static final KafkaContainer KAFKA_CONTAINER;

    private static final String KAFKA_DOCKER_IMAGE = "confluentinc/cp-kafka:7.6.1";

    static {
        KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse(KAFKA_DOCKER_IMAGE));
        KAFKA_CONTAINER.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    }
}
