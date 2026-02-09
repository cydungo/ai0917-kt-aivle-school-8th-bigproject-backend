package com.aivle.ai0917.ipai.domain.author.info.service;

import com.aivle.ai0917.ipai.domain.author.info.dto.AuthorNoticeDto;
import com.aivle.ai0917.ipai.domain.author.info.dto.AuthorNoticeDto.AuthorNoticeSource;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

public interface AuthorNoticeService {

    /**
     * SSE 구독 (Integration ID 사용)
     */
    SseEmitter subscribe(String integrationId);

    /**
     * [수정] Long 버전 제거됨
     * 알림 전송 (외부/편의용 - Integration ID 기반)
     * 이제 이 메서드 하나만 사용합니다.
     */
    void sendNotice(String integrationId, AuthorNoticeSource source, String title, String message, String url);

    /**
     * 알림 목록 조회
     */
    List<AuthorNoticeDto> getNotices(String integrationId, boolean onlyUnread);

    /**
     * 개별 알림 읽음 처리
     */
    void markAsRead(String integrationId, Long noticeId);

    /**
     * 모든 알림 읽음 처리
     */
    void markAllAsRead(String integrationId);
}