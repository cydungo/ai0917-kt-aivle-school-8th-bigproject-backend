package com.aivle.ai0917.ipai.domain.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 배포 및 환경 정보
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentInfoResponseDto {
    private String version; // "v2.4.1"
    private String environment; // "Production", "Staging", "Development"
    private String uptime; // "15 days 3 hours"
    private LocalDateTime lastDeployment;
}