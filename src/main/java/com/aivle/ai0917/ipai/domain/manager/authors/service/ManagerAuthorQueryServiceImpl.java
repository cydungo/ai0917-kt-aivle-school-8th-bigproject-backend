//package com.aivle.ai0917.ipai.domain.manager.authors.service;
//
//import com.aivle.ai0917.ipai.domain.admin.access.model.UserRole;
////import com.aivle.ai0917.ipai.domain.author.works.repository.WorkRepository;
//import com.aivle.ai0917.ipai.domain.manager.authors.dto.*;
//import com.aivle.ai0917.ipai.domain.manager.authors.repository.ManagerAuthorRepository;
//import com.aivle.ai0917.ipai.domain.user.model.User;
//import com.aivle.ai0917.ipai.domain.user.repository.UserRepository;
//import org.springframework.data.domain.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.stream.Collectors;
//
///**
// * ✅ 매니저 작가 조회 서비스 구현체
// * 흐름 공통:
// * 1) managerUserId(PK)로 User(매니저) 조회
// * 2) role이 Manager인지 체크
// * 3) 매니저 integrationId 추출
// * 4) users.role=Author AND users.managerIntegrationId = (매니저 integrationId) 조건으로 조회
// */
//@Service
//@Transactional(readOnly = true)
//public class ManagerAuthorQueryServiceImpl implements ManagerAuthorQueryService {
//
//    private final UserRepository userRepository;               // 로그인된 매니저(User PK) 조회용
//    private final ManagerAuthorRepository managerAuthorRepository; // "내 매칭 작가" 조회 전용 repo
////    private final WorkRepository workRepository;               // 작품 수, 최근 작품 조회용
//
////    public ManagerAuthorQueryServiceImpl(UserRepository userRepository,
////                                         ManagerAuthorRepository managerAuthorRepository,
////                                         WorkRepository workRepository) {
//    public ManagerAuthorQueryServiceImpl(UserRepository userRepository,
//        ManagerAuthorRepository managerAuthorRepository) {
//        this.userRepository = userRepository;
//        this.managerAuthorRepository = managerAuthorRepository;
////        this.workRepository = workRepository;
//    }
//
//    /**
//     * ✅ GET /api/v1/manager/authors/summary
//     * 요약 정보 반환
//     *
//     * 요구사항:
//     * - totalAuthors: role=Author인 내 매칭 작가 총합
//     * - newAuthors: createdAt이 최근 7일 이내
//     * - activeAuthors: lastActivityAt이 최근 1시간 이내
//     */
//    @Override
//    public AuthorSummaryResponseDto getSummary(Long managerUserId) {
//
//        // 1) 로그인된 매니저 PK로 User 엔티티 조회
//        User manager = userRepository.findById(managerUserId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        // 2) 권한 체크(매니저만 접근 가능)
//        if (manager.getRole() != UserRole.Manager) {
//            throw new RuntimeException("매니저(Manager)만 조회할 수 있습니다.");
//        }
//
//        // 3) 매니저의 integrationId 확보 (작가 users.managerIntegrationId와 비교할 값)
//        String mgrIntegrationId = manager.getIntegrationId();
//
//        LocalDateTime now = LocalDateTime.now();
//
//        // 4) 카운트 계산
//        long total = managerAuthorRepository.countByRoleAndManagerIntegrationId(UserRole.Author, mgrIntegrationId);
//
//        // 신규 작가: 최근 7일 내 가입 (createdAt 기준)
//        long newAuthors = managerAuthorRepository.countByRoleAndManagerIntegrationIdAndCreatedAtGreaterThanEqual(
//                UserRole.Author, mgrIntegrationId, now.minusDays(7)
//        );
//
//        // 활성 작가: 최근 1시간 내 활동(lastActivityAt 기준)
//        long activeAuthors = managerAuthorRepository.countByRoleAndManagerIntegrationIdAndLastActivityAtGreaterThanEqual(
//                UserRole.Author, mgrIntegrationId, now.minusHours(1)
//        );
//
//        return AuthorSummaryResponseDto.builder()
//                .totalAuthors(total)
//                .newAuthors(newAuthors)
//                .activeAuthors(activeAuthors)
//                .build();
//    }
//
//    /**
//     * ✅ GET /api/v1/manager/authors  (또는 /list)
//     * 작가 목록 조회 + 검색(keyword) + 정렬(sort) + 페이지(page,size)
//     *
//     * 프론트가 사용하는 필드:
//     * - id, name, email(siteEmail), workCount, createdAt, status(ACTIVE/INACTIVE)
//     *
//     * 정렬 요구사항:
//     * - 이름순/작품수순 정렬 포함
//     * - name/createdAt 정렬은 Pageable sort로 처리 가능
//     * - workCount 정렬은 User-Work 조인을 통한 별도 쿼리 필요
//     */
//    @Override
//    public Page<AuthorCardResponseDto> getAuthors(Long managerUserId, String keyword, Pageable pageable) {
//
//        // 1) 로그인된 매니저 PK로 조회
//        User manager = userRepository.findById(managerUserId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        // 2) 매니저만 가능
//        if (manager.getRole() != UserRole.Manager) {
//            throw new RuntimeException("매니저(Manager)만 조회할 수 있습니다.");
//        }
//
//        // 3) 매니저 integrationId
//        String mgrIntegrationId = manager.getIntegrationId();
//
//        // 4) keyword가 빈 문자열이면 검색 조건 제외하도록 null 처리
//        String searchKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
//
//        // 5) sort에 workCount가 포함되면 -> 전용 쿼리 사용
//        boolean sortByWorkCount = pageable.getSort().stream()
//                .anyMatch(o -> o.getProperty().equalsIgnoreCase("workCount"));
//
//        Page<User> userPage;
//
//        if (sortByWorkCount) {
//            // workCount 정렬 방향 (asc/desc)
//            Sort.Order order = pageable.getSort().getOrderFor("workCount");
//            boolean asc = (order != null && order.getDirection().isAscending());
//
//            // ⚠️ 전용 쿼리는 이미 ORDER BY COUNT(w)로 정렬하므로 Pageable에는 page/size만 사용
//            Pageable onlyPage = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
//
//            userPage = asc
//                    ? managerAuthorRepository.findMatchedAuthorsOrderByWorkCountAsc(
//                    mgrIntegrationId, searchKeyword, UserRole.Author, onlyPage
//            )
//                    : managerAuthorRepository.findMatchedAuthorsOrderByWorkCountDesc(
//                    mgrIntegrationId, searchKeyword, UserRole.Author, onlyPage
//            );
//
//        } else {
//            // name/createdAt 등은 JPA가 Pageable.sort대로 ORDER BY 처리
//            userPage = managerAuthorRepository.findMatchedAuthors(
//                    mgrIntegrationId, searchKeyword, UserRole.Author, pageable
//            );
//        }
//
//        // 6) status 판단 기준: lastActivityAt이 최근 1시간 이내면 ACTIVE
//        LocalDateTime activeThreshold = LocalDateTime.now().minusHours(1);
//
//        // 7) User -> AuthorCardResponseDto 변환 (workCount는 WorkRepository로 계산)
//        return userPage.map(u -> {
////            long workCount = workRepository.countByUserIntegrationId(u.getIntegrationId());
//
//            String status = (u.getLastActivityAt() != null && u.getLastActivityAt().isAfter(activeThreshold))
//                    ? "ACTIVE"
//                    : "INACTIVE";
//
//            return AuthorCardResponseDto.builder()
//                    .id(u.getId())
//                    .name(u.getName())
//                    .email(u.getSiteEmail())
////                    .workCount(workCount)
//                    .createdAt(u.getCreatedAt())
//                    .status(status)
//                    .build();
//        });
//    }
//
//    /**
//     * GET /api/v1/manager/authors/{id}
//     * 작가 상세 정보 + 최근 작품 5개
//     *
//     * 보안:
//     * - authorId가 존재하더라도 "내 매칭 작가"가 아니면 조회 불가
//     */
//    @Override
//    public AuthorDetailResponseDto getAuthorDetail(Long managerUserId, Long authorId) {
//
//        // 1) 매니저 조회
//        User manager = userRepository.findById(managerUserId)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        if (manager.getRole() != UserRole.Manager) {
//            throw new RuntimeException("매니저(Manager)만 조회할 수 있습니다.");
//        }
//
//        // 2) 내 integrationId 확보
//        String mgrIntegrationId = manager.getIntegrationId();
//
//        // 3) "내 매칭 작가"인지 조건을 걸고 조회
//        User author = managerAuthorRepository.findMatchedAuthorDetail(authorId, mgrIntegrationId, UserRole.Author)
//                .orElseThrow(() -> new RuntimeException("작가를 찾을 수 없거나 권한이 없습니다."));
//
//        // 4) 최근 작품 5개 조회 (works.userIntegrationId = author.integrationId)
////        var recentWorks = workRepository.findTop5ByUserIntegrationIdOrderByCreatedAtDesc(author.getIntegrationId())
////                .stream()
////                .map(w -> WorkSummaryDto.builder()
////                        .id(w.getId())
////                        .title(w.getTitle())
////                        .createdAt(w.getCreatedAt())
////                        .build())
////                .collect(Collectors.toList());
//
//        // 5) lastLogin(문자열) = lastActivityAt을 문자열로 변환
//        String lastLogin = (author.getLastActivityAt() == null) ? null : author.getLastActivityAt().toString();
//
//        return AuthorDetailResponseDto.builder()
//                .id(author.getId())
//                .name(author.getName())
//                .email(author.getSiteEmail())
//                .gender(author.getGender())
//                .birthYear(author.getBirthYear())
//                .birthday(author.getBirthday())
//                .createdAt(author.getCreatedAt())
//                .lastLogin(lastLogin)
////                .recentWorks(recentWorks)
//                .build();
//    }
//}
