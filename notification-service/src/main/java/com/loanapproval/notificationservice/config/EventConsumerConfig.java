package com.loanapproval.notificationservice.config;

import com.loanapproval.notificationservice.dto.NotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumerConfig {
    private static final Logger log = LoggerFactory.getLogger(EventConsumerConfig.class);

    @Bean
    public Consumer<NotificationRequest> processMessage() {
        return message -> {
            log.info("Received message: {}", message.getMessage());
        };
    }
}
