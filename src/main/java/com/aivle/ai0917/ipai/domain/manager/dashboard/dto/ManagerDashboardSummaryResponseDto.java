package com.aivle.ai0917.ipai.domain.manager.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDashboardSummaryResponseDto {
    private long pendingProposals;
    private long managedAuthors;
    private long activeAuthors;
}