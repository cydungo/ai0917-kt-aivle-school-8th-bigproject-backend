
package com.aivle.ai0917.ipai.domain.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 대시보드 페이지 전체 응답
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardPageResponseDto {
    private DashboardSummaryResponseDto summary;
    private DauResponseDto dau;
    private ResourceUsageResponseDto resources;
    private SystemLogsResponseDto logs;
    private DeploymentInfoResponseDto deployment;
}
