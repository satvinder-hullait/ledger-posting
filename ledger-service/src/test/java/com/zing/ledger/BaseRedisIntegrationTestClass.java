package com.zing.ledger;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class BaseRedisIntegrationTestClass {

    protected static final GenericContainer<?> REDIS_CONTAINER;

    private static final String REDIS_DOCKER_IMAGE = "redis:7.2.5-alpine";

    static {
        REDIS_CONTAINER =
                new GenericContainer<>(DockerImageName.parse(REDIS_DOCKER_IMAGE))
                        .withExposedPorts(6379);
        REDIS_CONTAINER.start();
        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
    }

    //    @DynamicPropertySource
    //    static void setProperties(DynamicPropertyRegistry registry) {
    //        registry.add("spring.kafka.bootstrap-servers", KAFKA_CONTAINER::getBootstrapServers);
    //    }

}
