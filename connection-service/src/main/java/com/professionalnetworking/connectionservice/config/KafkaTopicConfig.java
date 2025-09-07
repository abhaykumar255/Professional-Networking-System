package com.professionalnetworking.connectionservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic connectionRequestTopic() {
        return new NewTopic("connection-request-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic connectionAcceptanceTopic() {
        return new NewTopic("connection-acceptance-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic connectionRejectionTopic() {
        return new NewTopic("connection-rejection-topic", 3, (short) 1);
    }
}
