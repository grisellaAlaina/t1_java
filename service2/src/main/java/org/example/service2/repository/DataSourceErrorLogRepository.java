package org.example.service2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.service2.model.DataSourceErrorLog;

public interface DataSourceErrorLogRepository extends JpaRepository<DataSourceErrorLog, Long> {

    @Override
    <S extends DataSourceErrorLog> S saveAndFlush(S entity);
}
