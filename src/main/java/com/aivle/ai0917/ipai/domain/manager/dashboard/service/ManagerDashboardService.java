package com.aivle.ai0917.ipai.domain.manager.dashboard.service;

import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardPageResponseDto;
import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardSummaryResponseDto;

public interface ManagerDashboardService {
    ManagerDashboardPageResponseDto getDashboardPage(Long managerUserId);

    ManagerDashboardSummaryResponseDto getDashboardSummary(Long managerUserId);
}