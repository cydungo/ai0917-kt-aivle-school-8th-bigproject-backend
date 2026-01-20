package com.aivle.ai0917.ipai.domain.admin.dashboard.repository;

import com.aivle.ai0917.ipai.domain.admin.dashboard.model.DailyActiveUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyActiveUserRepository extends JpaRepository<DailyActiveUser, Long> {

    // 1. 특정 날짜 조회 (Instant 사용)
    @Query("SELECT d FROM DailyActiveUser d WHERE d.date = CAST(:date AS timestamp)")
    Optional<DailyActiveUser> findByDate(@Param("date") LocalDateTime date);

    // 2. 날짜 범위 조회 (Instant 사용)
    @Query("SELECT d FROM DailyActiveUser d WHERE d.date BETWEEN :startDate AND :endDate ORDER BY d.date DESC")
    List<DailyActiveUser> findByDateBetweenOrderByDateDesc(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // 3. 최근 N일 조회 (Pageable은 그대로 유지)
    List<DailyActiveUser> findAllByOrderByDateDesc(Pageable pageable);

    // 4. 7일 평균 계산 (Instant 사용)
    @Query("SELECT AVG(d.count) FROM DailyActiveUser d " +
            "WHERE d.date BETWEEN :startDate AND :endDate")
    Double calculateAverageByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}