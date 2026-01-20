package com.aivle.ai0917.ipai.domain.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

// 리소스 사용량
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceUsageResponseDto {
    private Double cpuUsage; // 0-100 (%)
    private Double memoryUsage; // 0-100 (%)
    private Double storageUsage; // 0-100 (%)
    private LocalDateTime timestamp;
}