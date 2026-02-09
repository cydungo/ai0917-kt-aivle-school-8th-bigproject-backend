package com.aivle.ai0917.ipai.domain.author.info.controller;

import com.aivle.ai0917.ipai.domain.author.info.dto.AuthorNoticeDto;
import com.aivle.ai0917.ipai.domain.author.info.service.AuthorNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/author/sysnotice")
@RequiredArgsConstructor
public class AuthorNoticeController {

    private final AuthorNoticeService authorNoticeService;

    /**
     * [GET] 최근 알림 목록 조회
     * 사용법: GET /api/v1/author/sysnotice?integrationId=user1234&all=false
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getNotices(
            @RequestParam String integrationId, // 8자리 문자열 ID
            @RequestParam(defaultValue = "false") boolean all) {

        List<AuthorNoticeDto> notices = authorNoticeService.getNotices(integrationId, !all);

        Map<String, Object> response = new HashMap<>();
        response.put("notices", notices);
        response.put("count", notices.size());

        return ResponseEntity.ok(response);
    }

    /**
     * [GET] 실시간 알림 구독 (SSE)
     * 사용법: GET /api/v1/author/sysnotice/subscribe?integrationId=user1234
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam String integrationId) {
        System.out.println(">>> 구독 요청 들어옴! ID: " + integrationId);
        return authorNoticeService.subscribe(integrationId);
    }

    /**
     * [PATCH] 개별 알림 읽음 처리
     * 사용법: PATCH /api/v1/author/sysnotice/{id}/read?integrationId=user1234
     */
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(
            @RequestParam String integrationId, // 본인 확인용 ID
            @PathVariable Long id) {

        authorNoticeService.markAsRead(integrationId, id);
        return ResponseEntity.ok().build();
    }

    /**
     * [PATCH] 알림 모두 읽음 처리
     * 사용법: PATCH /api/v1/author/sysnotice/read-all?integrationId=user1234
     */
    @PatchMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@RequestParam String integrationId) {
        authorNoticeService.markAllAsRead(integrationId);
        return ResponseEntity.ok().build();
    }
}