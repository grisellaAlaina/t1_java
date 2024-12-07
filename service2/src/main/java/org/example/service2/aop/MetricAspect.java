package org.example.service2.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.service2.exception.KafkaSendException;
import org.springframework.stereotype.Component;
import org.example.service2.dto.MetricLogDto;
import org.example.service2.kafka.MetricProducer;

@Aspect
@Component
@Slf4j
public class MetricAspect {

    private final MetricProducer producer;

    public MetricAspect(MetricProducer producer) {
        this.producer = producer;
    }

    @Around("@annotation(metric)")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint, Metric metric) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

            if (executionTime > metric.value()) {
                log.warn("Method {} took {} ms which exceeds the limit of {} ms",
                        joinPoint.getSignature().toShortString(), executionTime, metric.value());

                MetricLogDto metricLog = MetricLogDto.builder()
                        .methodName(joinPoint.getSignature().toShortString())
                        .params(getMethodParams(joinPoint.getArgs()))
                        .executionTime(executionTime)
                        .build();

                try {
                    producer.send(metricLog);
                } catch (KafkaSendException kafkaException) {
                    log.error("Failed to send message to Kafka");
                }
            }
        }

        return result;
    }


    private String getMethodParams(Object[] args) {
        if (args == null || args.length == 0) {
            return "No parameters";
        }
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if (params.length() > 0) {
                params.append(", ");
            }
            params.append(arg.getClass().getSimpleName()).append(": ").append(arg);
        }
        return params.toString();
    }
}