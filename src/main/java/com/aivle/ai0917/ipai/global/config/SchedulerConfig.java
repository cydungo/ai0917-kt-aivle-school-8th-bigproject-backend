package com.aivle.ai0917.ipai.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 스케줄러 활성화 설정
 */
@Configuration
@EnableScheduling
public class SchedulerConfig {
    // @Scheduled 어노테이션 활성화
}