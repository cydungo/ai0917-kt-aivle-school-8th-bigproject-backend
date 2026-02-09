package com.aivle.ai0917.ipai.domain.author.manager.controller;

import com.aivle.ai0917.ipai.global.security.jwt.CurrentUserId;
import com.aivle.ai0917.ipai.domain.author.manager.dto.AuthorManagerResponseDto;
import com.aivle.ai0917.ipai.domain.author.manager.service.AuthorManagerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

/**
 * ✅ 작가가 "내 매니저"를 조회하는 API
 *
 * GET /api/v1/author/manager
 */
@RestController
@RequestMapping("/api/v1/author")
public class AuthorManagerController {

    private final AuthorManagerService authorManagerService;

    public AuthorManagerController(AuthorManagerService authorManagerService) {
        this.authorManagerService = authorManagerService;
    }

    /**
     * GET /api/v1/author/manager
     * - 로그인된 사용자의 PK(Long)를 Authentication에서 꺼냄
     * - Service로 전달해 "내 매니저"를 조회
     */
    @GetMapping("/manager")
    public AuthorManagerResponseDto getMyManager(@CurrentUserId Long userId) {
        return authorManagerService.getMyManager(userId);
    }

    /**
     * DELETE /api/v1/author/manager
     * - 로그인된 작가의 매니저 매칭 삭제
     */
    @DeleteMapping("/manager")
    public ResponseEntity<Void> deleteMyManager(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }
        authorManagerService.deleteMyManager(userId);
        return ResponseEntity.noContent().build();
    }

}
