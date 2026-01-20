package com.aivle.ai0917.ipai.domain.admin.dashboard.controller;

import com.aivle.ai0917.ipai.domain.admin.dashboard.dto.*;
import com.aivle.ai0917.ipai.domain.admin.dashboard.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    /**
     * 관리자 대시보드 페이지
     */
    @GetMapping
    public ResponseEntity<DashboardPageResponseDto> getDashboard() {
        DashboardPageResponseDto response = adminDashboardService.getDashboardPage();
        return ResponseEntity.ok(response);
    }

    /**
     * 관리자 대시보드 통계 (상단 4개 카드)
     */
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponseDto> getSummary() {
        DashboardSummaryResponseDto response = adminDashboardService.getDashboardSummary();
        return ResponseEntity.ok(response);
    }

    /**
     * 일일 활성 사용자(DAU) 데이터
     */
    @GetMapping("/dau")
    public ResponseEntity<DauResponseDto> getDau() {
        DauResponseDto response = adminDashboardService.getDauData();
        return ResponseEntity.ok(response);
    }

    /**
     * 시스템 리소스 사용량 조회
     */
    @GetMapping("/resources")
    public ResponseEntity<ResourceUsageResponseDto> getResources() {
        ResourceUsageResponseDto response = adminDashboardService.getResourceUsage();
        return ResponseEntity.ok(response);
    }

    /**
     * 최근 시스템 로그 목록 조회
     */
    @GetMapping("/logs")
    public ResponseEntity<SystemLogsResponseDto> getLogs(
            @RequestParam(defaultValue = "20") int limit) {
        SystemLogsResponseDto response = adminDashboardService.getRecentLogs(limit);
        return ResponseEntity.ok(response);
    }

    /**
     * 배포 및 환경 정보 조회
     */
    @GetMapping("/deployment")
    public ResponseEntity<DeploymentInfoResponseDto> getDeployment() {
        DeploymentInfoResponseDto response = adminDashboardService.getDeploymentInfo();
        return ResponseEntity.ok(response);
    }
}