package com.aivle.ai0917.ipai.domain.admin.dashboard.listener;

import com.aivle.ai0917.ipai.domain.admin.dashboard.model.DeploymentInfo;
import com.aivle.ai0917.ipai.domain.admin.dashboard.repository.DeploymentInfoRepository;
import com.aivle.ai0917.ipai.domain.admin.dashboard.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;


/**
 * 애플리케이션 시작 시 배포 정보 기록
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupListener {

    private final DeploymentInfoRepository deploymentInfoRepository;
    private final SystemLogService systemLogService;


    @Value("${custom.build.version:v2.4.1}")
    private String appVersion;

    @Value("${spring.profiles.active:Production}")
    private String environment;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            log.info("Recording deployment information...");

            LocalDateTime now = LocalDateTime.now();

            // appVersion 변수를 사용하여 저장합니다.
            DeploymentInfo deploymentInfo = DeploymentInfo.builder()
                    .version(appVersion)
                    .environment(environment)
                    .deploymentTime(now)
                    .serverStartTime(now)
                    .deployedBy("System")
                    .releaseNotes("Application started successfully")
                    .build();

            deploymentInfoRepository.save(deploymentInfo);

            // 시스템 로그 기록
            systemLogService.logInfo(
                    "APPLICATION_STARTUP",
                    String.format("Application started - Version: %s, Environment: %s", appVersion, environment)
            );

            log.info("Deployment info saved: Version={}, Environment={}", appVersion, environment);

        } catch (Exception e) {
            log.error("Failed to record deployment information", e);
        }
    }
}