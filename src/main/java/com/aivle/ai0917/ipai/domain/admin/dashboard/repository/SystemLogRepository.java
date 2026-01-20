package com.aivle.ai0917.ipai.domain.admin.dashboard.repository;

import com.aivle.ai0917.ipai.domain.admin.dashboard.model.SystemLog;
import org.springframework.data.domain.Pageable; // 추가
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {

    // 최근 로그 조회 (Pageable 사용)
    List<SystemLog> findAllByOrderByTimestampDesc(Pageable pageable);

    // 특정 레벨의 로그 조회
    List<SystemLog> findByLevelOrderByTimestampDesc(String level, Pageable pageable);

    // 특정 카테고리의 로그 조회
    List<SystemLog> findByCategoryOrderByTimestampDesc(String category, Pageable pageable);

    // 날짜 범위로 로그 조회
    List<SystemLog> findByTimestampBetweenOrderByTimestampDesc(
            LocalDateTime start, LocalDateTime end, Pageable pageable);
}
