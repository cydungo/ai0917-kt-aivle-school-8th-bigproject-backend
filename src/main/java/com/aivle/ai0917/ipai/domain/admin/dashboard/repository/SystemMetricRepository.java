package com.aivle.ai0917.ipai.domain.admin.dashboard.repository;
import com.aivle.ai0917.ipai.domain.admin.dashboard.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SystemMetricRepository extends JpaRepository<SystemMetric, Long> {

    // 가장 최근 메트릭 조회
    Optional<SystemMetric> findTopByOrderByTimestampDesc();

    // 특정 시간 이후의 메트릭 조회
    List<SystemMetric> findByTimestampAfterOrderByTimestampDesc(LocalDateTime timestamp);

    // 날짜 범위로 메트릭 조회
    List<SystemMetric> findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime start, LocalDateTime end);

    // 평균 리소스 사용량 계산
    @Query("SELECT AVG(m.cpuUsage) FROM SystemMetric m " +
            "WHERE m.timestamp >= :startTime")
    Double calculateAverageCpuUsage(@Param("startTime") LocalDateTime startTime);
}