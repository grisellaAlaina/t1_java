package org.example.service2.service;

import org.example.service2.repository.DataSourceErrorLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.service2.model.DataSourceErrorLog;

@Service
public class DataSourceErrorLogService {

    private final DataSourceErrorLogRepository dataSourceErrorLogRepository;

    @Autowired
    public DataSourceErrorLogService(DataSourceErrorLogRepository dataSourceErrorLogRepository) {
        this.dataSourceErrorLogRepository = dataSourceErrorLogRepository;
    }

    @Transactional
    public DataSourceErrorLog save(DataSourceErrorLog log) {
        return dataSourceErrorLogRepository.save(log);
    }
}