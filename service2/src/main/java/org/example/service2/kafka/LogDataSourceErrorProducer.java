package org.example.service2.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.example.service2.exception.KafkaSendException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogDataSourceErrorProducer {

    private static final String TOPIC_NAME = "t1_demo_metrics";
    private static final String ERROR_TYPE = "DATA_SOURCE";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendErrorLog(String errorLog) throws KafkaSendException {
        Message<String> message = MessageBuilder.withPayload(errorLog)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .setHeader("errorType", ERROR_TYPE)
                .build();

        try {
            kafkaTemplate.send(message).get();
            log.info("Error message sent to Kafka topic: {}", TOPIC_NAME);
        } catch (Exception ex) {
            log.error("Kafka message send failed: {}", ex.getMessage(), ex);
            throw new KafkaSendException("Failed to send error log to Kafka", ex);
        }
    }
}