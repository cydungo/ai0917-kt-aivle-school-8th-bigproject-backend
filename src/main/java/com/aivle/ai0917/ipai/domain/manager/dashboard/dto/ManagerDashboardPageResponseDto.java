package com.aivle.ai0917.ipai.domain.manager.dashboard.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDashboardPageResponseDto {
    private ManagerDashboardSummaryResponseDto summary;
    private List<ManagerDashboardNoticeDto> notices;
}