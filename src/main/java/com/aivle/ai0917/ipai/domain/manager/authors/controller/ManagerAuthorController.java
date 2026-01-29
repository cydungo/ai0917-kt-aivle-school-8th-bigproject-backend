package com.aivle.ai0917.ipai.domain.manager.authors.controller;

import com.aivle.ai0917.ipai.domain.manager.authors.service.ManagerAuthorQueryService;
import com.aivle.ai0917.ipai.domain.manager.authors.service.ManagerAuthorService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.aivle.ai0917.ipai.domain.manager.authors.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/manager")
public class ManagerAuthorController {

    private final ManagerAuthorService managerAuthorService;
    private final ManagerAuthorQueryService managerAuthorQueryService;

    public ManagerAuthorController(ManagerAuthorService managerAuthorService, ManagerAuthorQueryService managerAuthorQueryService) {
        this.managerAuthorService = managerAuthorService;
        this.managerAuthorQueryService = managerAuthorQueryService;
    }

    // POST /api/v1/manager/author/{pwd}
    @PostMapping("/authors/{pwd}")
    public Map<String, Object> matchAuthor(@PathVariable("pwd") String pwd,
                                           Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return managerAuthorService.matchAuthorByInviteCode(userId, pwd);
    }

    /**
     * ✅ 2) 작가 현황 요약 조회
     * GET /api/v1/manager/authors/summary
     */
    @GetMapping("/summary")
    public AuthorSummaryResponseDto summary(Authentication authentication) {
        Long managerUserId = extractUserId(authentication);
        return managerAuthorQueryService.getSummary(managerUserId);
    }

    /**
     * ✅ 1) 작가 목록 조회 + 검색/정렬/페이지
     * GET /api/v1/manager/authors?keyword=...&page=0&size=10&sort=createdAt,desc
     * - sort=name,asc / sort=workCount,desc 등 가능
     */
    @GetMapping
    public Page<AuthorCardResponseDto> list(@RequestParam(required = false) String keyword,
                                            Pageable pageable,
                                            Authentication authentication) {
        Long managerUserId = extractUserId(authentication);
        return managerAuthorQueryService.getAuthors(managerUserId, keyword, pageable);
    }

    /**
     * ✅ (호환용) 프론트가 /list로 호출하던 경우를 지원
     * GET /api/v1/manager/authors/list?...
     */
    @GetMapping("/list")
    public Page<AuthorCardResponseDto> listAlias(@RequestParam(required = false) String keyword,
                                                 Pageable pageable,
                                                 Authentication authentication) {
        Long managerUserId = extractUserId(authentication);
        return managerAuthorQueryService.getAuthors(managerUserId, keyword, pageable);
    }

    /**
     * ✅ 3) 작가 상세 조회
     * GET /api/v1/manager/authors/{id}
     * - id는 users PK(Long)
     */
    @GetMapping("/{id}")
    public AuthorDetailResponseDto detail(@PathVariable Long id,
                                          Authentication authentication) {
        Long managerUserId = extractUserId(authentication);
        return managerAuthorQueryService.getAuthorDetail(managerUserId, id);
    }

    /**
     * Authentication에서 현재 로그인 유저 PK(Long)를 꺼내는 공통 메서드
     * - JwtAuthFilter가 SecurityContext에 userId(PK)를 principal로 넣어둔 상태여야 함
     */
    private Long extractUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        return userId;
    }
}
