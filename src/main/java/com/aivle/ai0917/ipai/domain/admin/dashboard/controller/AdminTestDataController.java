//package com.aivle.ai0917.ipai.domain.admin.dashboard.controller;
//
//import com.aivle.ai0917.ipai.domain.admin.dashboard.service.SystemLogService;
//import com.aivle.ai0917.ipai.domain.admin.dashboard.model.*;
//import com.aivle.ai0917.ipai.domain.admin.dashboard.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Random;
//
///**
// * 테스트 데이터 생성용 임시 API
// * 개발/테스트 환경에서만 사용
// */
//@RestController
//@RequestMapping("/api/v1/admin/test-data")
//@RequiredArgsConstructor
//public class AdminTestDataController {
//
//    private final SystemMetricRepository systemMetricRepository;
//    private final DailyActiveUserRepository dauRepository;
//    private final SystemLogService systemLogService;
//    private final DeploymentInfoRepository deploymentInfoRepository;
//
//    private final Random random = new Random();
//
//    /**
//     * 전체 테스트 데이터 생성
//     */
//    @PostMapping("/generate-all")
//    public ResponseEntity<Map<String, Object>> generateAllTestData() {
//        Map<String, Object> result = new HashMap<>();
//
//        int metrics = generateSystemMetrics();
//        int dau = generateDauData();
//        int logs = generateSystemLogs();
//        int deployment = generateDeploymentInfo();
//
//        result.put("systemMetrics", metrics);
//        result.put("dauRecords", dau);
//        result.put("systemLogs", logs);
//        result.put("deploymentInfo", deployment);
//        result.put("message", "Test data generated successfully");
//
//        return ResponseEntity.ok(result);
//    }
//
//    /**
//     * 시스템 메트릭 테스트 데이터 생성 API
//     * 메서드 명 중복 회피를 위해 'Api' 접미사 추가
//     */
//    @PostMapping("/system-metrics")
//    public ResponseEntity<Map<String, Object>> generateSystemMetricsApi() {
//        int count = generateSystemMetrics(); // 실제 로직 수행하는 private 메서드 호출
//        return ResponseEntity.ok(Map.of(
//                "generated", count,
//                "message", "System metrics test data generated"
//        ));
//    }
//
//    private int generateSystemMetrics() {
//        LocalDateTime now = LocalDateTime.now();
//        int count = 0;
//
//        for (int i = 287; i >= 0; i--) {
//            LocalDateTime timestamp = now.minusMinutes(i * 5L);
//
//            SystemMetric metric = SystemMetric.builder()
//                    .cpuUsage(20.0 + random.nextDouble() * 60.0)
//                    .memoryUsage(40.0 + random.nextDouble() * 40.0)
//                    .storageUsage(50.0 + random.nextDouble() * 20.0)
//                    .timestamp(timestamp)
//                    .build();
//
//            systemMetricRepository.save(metric);
//            count++;
//        }
//        return count;
//    }
//
//    /**
//     * DAU 테스트 데이터 생성 API
//     */
//    @PostMapping("/dau")
//    public ResponseEntity<Map<String, Object>> generateDauDataApi() {
//        int count = generateDauData();
//        return ResponseEntity.ok(Map.of(
//                "generated", count,
//                "message", "DAU test data generated"
//        ));
//    }
//
//    private int generateDauData() {
//        LocalDate today = LocalDate.now();
//        int count = 0;
//
//        for (int i = 29; i >= 0; i--) {
//            LocalDate date = today.minusDays(i);
//
//            DailyActiveUser dau = DailyActiveUser.builder()
//                    .date(date.atStartOfDay())
//                    .count(50 + random.nextInt(100))
//                    .createdAt(LocalDateTime.now())
//                    .build();
//
//            dauRepository.save(dau);
//            count++;
//        }
//        return count;
//    }
//
//    /**
//     * 시스템 로그 테스트 데이터 생성 API
//     */
//    @PostMapping("/system-logs")
//    public ResponseEntity<Map<String, Object>> generateSystemLogsApi() {
//        int count = generateSystemLogs();
//        return ResponseEntity.ok(Map.of(
//                "generated", count,
//                "message", "System logs test data generated"
//        ));
//    }
//
//    private int generateSystemLogs() {
//        String[] categories = {"DB_BACKUP", "ACCOUNT_CREATED", "API_DELAY", "PERMISSION_CHANGED", "LOGIN_FAILED", "DATA_EXPORT"};
//        String[] levels = {"INFO", "WARNING", "ERROR"};
//        String[] messages = {
//                "Database backup completed successfully", "New user account created",
//                "API response time exceeded threshold", "User permission updated",
//                "Failed login attempt detected", "Data export requested"
//        };
//
//        int count = 0;
//        for (int i = 0; i < 50; i++) {
//            String category = categories[random.nextInt(categories.length)];
//            String level = levels[random.nextInt(levels.length)];
//            String message = messages[random.nextInt(messages.length)];
//
//            if (level.equals("INFO")) {
//                systemLogService.logInfo(category, message);
//            } else if (level.equals("WARNING")) {
//                systemLogService.logWarning(category, message);
//            } else {
//                systemLogService.logError(category, message);
//            }
//            count++;
//        }
//        return count;
//    }
//
//    /**
//     * 배포 정보 테스트 데이터 생성 API
//     */
//    @PostMapping("/deployment-info")
//    public ResponseEntity<Map<String, Object>> generateDeploymentInfoApi() {
//        int count = generateDeploymentInfo();
//        return ResponseEntity.ok(Map.of(
//                "generated", count,
//                "message", "Deployment info test data generated"
//        ));
//    }
//
//    private int generateDeploymentInfo() {
//        if (deploymentInfoRepository.count() == 0) {
//            DeploymentInfo deployment = DeploymentInfo.builder()
//                    .version("v2.4.1")
//                    .environment("Production")
//                    .deploymentTime(LocalDateTime.now().minusDays(15))
//                    .serverStartTime(LocalDateTime.now().minusDays(15))
//                    .deployedBy("Admin")
//                    .releaseNotes("Initial deployment with dashboard features")
//                    .build();
//
//            deploymentInfoRepository.save(deployment);
//            return 1;
//        }
//        return 0;
//    }
//
//    /**
//     * 모든 테스트 데이터 삭제
//     */
//    @DeleteMapping("/clear-all")
//    public ResponseEntity<Map<String, String>> clearAllTestData() {
//        systemMetricRepository.deleteAll();
//        dauRepository.deleteAll();
//        return ResponseEntity.ok(Map.of("message", "All test data cleared successfully"));
//    }
//}