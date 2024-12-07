package org.example.service2.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.service2.exception.KafkaSendException;
import org.example.service2.repository.DataSourceErrorLogRepository;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.example.service2.kafka.LogDataSourceErrorProducer;
import org.example.service2.model.DataSourceErrorLog;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(0)
public class LogDataSourceErrorAspect {

    private final DataSourceErrorLogRepository repository;

    private final LogDataSourceErrorProducer producer;

    @Pointcut("within(org.example.service2.*)")
    public void dataSourceErrorLogMethods() {}

    @AfterThrowing(pointcut = "@annotation(org.example.service2.aop.LogDataSourceError)", throwing = "e")
    public void logDataSourceErrorAdvice(JoinPoint joinPoint, Exception e) {

        log.error("logDataSourceErrorAdvice: Data source exception - " + e.getMessage());

        try {
            producer.sendErrorLog(e.getMessage());
        } catch (KafkaSendException kafkaException) {

            log.error("logDataSourceErrorAdvice: Data source exception - " + e.getMessage());

        repository.saveAndFlush(DataSourceErrorLog.builder()
                .message(e.getMessage())
                .methodSignature(String.valueOf(joinPoint.getSignature()))
                .stackTrace(Arrays.toString(e.getStackTrace()))
                .build());
    }
}
}


