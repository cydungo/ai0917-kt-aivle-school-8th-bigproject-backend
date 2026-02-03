package com.aivle.ai0917.ipai.domain.manager.dashboard.controller;

import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardPageResponseDto;
import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardSummaryResponseDto;
import com.aivle.ai0917.ipai.domain.manager.dashboard.service.ManagerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager/dashboard")
@RequiredArgsConstructor
public class ManagerDashboardController {

    private final ManagerDashboardService managerDashboardService;

    /**
     * 운영자 대시보드 페이지
     */
    @GetMapping
    public ResponseEntity<ManagerDashboardPageResponseDto> getDashboard(Authentication authentication) {
        Long managerUserId = extractUserId(authentication);
        ManagerDashboardPageResponseDto response = managerDashboardService.getDashboardPage(managerUserId);
        return ResponseEntity.ok(response);
    }

    /**
     * 운영자 대시보드 요약
     */
    @GetMapping("/summary")
    public ResponseEntity<ManagerDashboardSummaryResponseDto> getSummary(Authentication authentication) {
        Long managerUserId = extractUserId(authentication);
        ManagerDashboardSummaryResponseDto response = managerDashboardService.getDashboardSummary(managerUserId);
        return ResponseEntity.ok(response);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return userId;
    }
}