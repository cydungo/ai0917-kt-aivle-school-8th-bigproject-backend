package com.aivle.ai0917.ipai.domain.admin.dashboard.service;

import com.aivle.ai0917.ipai.domain.admin.dashboard.model.SystemLog;
import com.aivle.ai0917.ipai.domain.admin.dashboard.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 시스템 로그 기록 서비스
 * 비즈니스 로직에서 중요 이벤트 발생 시 로그를 DB에 저장
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemLogService {

    private final SystemLogRepository systemLogRepository;

    /**
     * INFO 레벨 로그 기록
     */
    @Transactional
    public void logInfo(String category, String message) {
        saveLog("INFO", category, message, null);
    }

    /**
     * INFO 레벨 로그 기록 (메타데이터 포함)
     */
    @Transactional
    public void logInfo(String category, String message, String metadata) {
        saveLog("INFO", category, message, metadata);
    }

    /**
     * WARNING 레벨 로그 기록
     */
    @Transactional
    public void logWarning(String category, String message) {
        saveLog("WARNING", category, message, null);
    }

    /**
     * WARNING 레벨 로그 기록 (메타데이터 포함)
     */
    @Transactional
    public void logWarning(String category, String message, String metadata) {
        saveLog("WARNING", category, message, metadata);
    }

    /**
     * ERROR 레벨 로그 기록
     */
    @Transactional
    public void logError(String category, String message) {
        saveLog("ERROR", category, message, null);
    }

    /**
     * ERROR 레벨 로그 기록 (메타데이터 포함)
     */
    @Transactional
    public void logError(String category, String message, String metadata) {
        saveLog("ERROR", category, message, metadata);
    }

    /**
     * 시스템 로그 저장
     */
    private void saveLog(String level, String category, String message, String metadata) {
        try {
            SystemLog systemLog = SystemLog.builder()
                    .level(level)
                    .category(category)
                    .message(message)
                    .metadata(metadata)
                    .timestamp(LocalDateTime.now())
                    .build();

            systemLogRepository.save(systemLog);
            log.debug("System log saved: [{}] {} - {}", level, category, message);

        } catch (Exception e) {
            // 로그 저장 실패 시에도 애플리케이션은 계속 동작하도록
            log.error("Failed to save system log: {} - {}", category, message, e);
        }
    }
}