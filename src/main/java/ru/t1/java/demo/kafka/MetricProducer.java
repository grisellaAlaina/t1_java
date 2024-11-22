package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.dto.MetricLogDto;

@Slf4j
@Component
@RequiredArgsConstructor
public class MetricProducer {

    private static final String TOPIC_NAME = "t1_demo_metrics";
    private static final String ERR_TYPE = "METRICS";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(MetricLogDto metricLog) {
        Message<MetricLogDto> message = MessageBuilder.withPayload(metricLog)
                .setHeader(KafkaHeaders.TOPIC, TOPIC_NAME)
                .setHeader("errorType", ERR_TYPE)
                .build();

        try {
            kafkaTemplate.send(message);
            kafkaTemplate.flush();
            log.info("Metric log sent to Kafka topic: {}", TOPIC_NAME);
        } catch (Exception ex) {
            log.error("Failed to send metric log to Kafka: {}", ex.getMessage(), ex);
        }
    }
}