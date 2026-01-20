package com.aivle.ai0917.ipai.domain.admin.dashboard.service;

import com.aivle.ai0917.ipai.domain.admin.dashboard.dto.*;

public interface AdminDashboardService {

    /**
     * 관리자 대시보드 전체 페이지 데이터 조회
     */
    DashboardPageResponseDto getDashboardPage();

    /**
     * 대시보드 요약 정보 조회 (상단 4개 카드)
     * - 서버 상태
     * - 총 사용자 수
     * - 저장된 작품 수
     * - 활성 세션 수
     */
    DashboardSummaryResponseDto getDashboardSummary();

    /**
     * 일일 활성 사용자(DAU) 데이터 조회
     * - 오늘 DAU
     * - 어제 DAU
     * - 7일 평균 DAU
     * - 날짜별 DAU 데이터
     */
    DauResponseDto getDauData();

    /**
     * 시스템 리소스 사용량 조회
     * - CPU 사용률
     * - 메모리 사용률
     * - 스토리지 사용률
     */
    ResourceUsageResponseDto getResourceUsage();

    /**
     * 최근 시스템 로그 목록 조회
     * @param limit 조회할 로그 개수
     */
    SystemLogsResponseDto getRecentLogs(int limit);

    /**
     * 배포 및 환경 정보 조회
     * - 현재 버전
     * - 배포 환경
     * - 가동 시간(Uptime)
     */
    DeploymentInfoResponseDto getDeploymentInfo();
}