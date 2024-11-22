package ru.t1.java.demo.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MetricLogDto {
    private String methodName;
    private long executionTime;
    private String params;
}