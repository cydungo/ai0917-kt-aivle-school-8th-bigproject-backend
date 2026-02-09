package com.aivle.ai0917.ipai.domain.author.info.service;

import com.aivle.ai0917.ipai.domain.author.info.dto.AuthorNoticeDto;
import com.aivle.ai0917.ipai.domain.author.info.dto.AuthorNoticeDto.AuthorNoticeSource;
import com.aivle.ai0917.ipai.domain.author.info.model.AuthorNotice;
import com.aivle.ai0917.ipai.domain.author.info.repository.AuthorNoticeRepository;
import com.aivle.ai0917.ipai.domain.user.model.User;
import com.aivle.ai0917.ipai.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorNoticeServiceImpl implements AuthorNoticeService {

    private final AuthorNoticeRepository authorNoticeRepository;
    private final UserRepository userRepository; // [추가] ID 조회를 위해 필요

    // 내부적으로는 여전히 Long PK를 Key로 사용하는 것이 관리상 안전합니다.
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
    private static final long SSE_TIMEOUT = 60 * 60 * 1000L;

    @Override
    public SseEmitter subscribe(String integrationId) {
        Long authorId = findAuthorIdByIntegrationId(integrationId);

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);
        emitters.put(authorId, emitter);

        // 타임아웃/완료/에러 시 제거 로직
        emitter.onCompletion(() -> emitters.remove(authorId));
        emitter.onTimeout(() -> emitters.remove(authorId));
        emitter.onError((e) -> emitters.remove(authorId));

        // 연결 즉시 더미 데이터 전송 (503 에러 방지 및 연결 확인용)
        sendToClient(authorId, "connected", "SSE Connected [" + integrationId + "]");
        return emitter;
    }

    @Override
    @Transactional
    public void sendNotice(String integrationId, AuthorNoticeSource source, String title, String message, String url) {
        try {
            Long authorId = findAuthorIdByIntegrationId(integrationId);
            // 내부 private 메서드 호출
            sendNoticeInternal(authorId, source, title, message, url);
        } catch (Exception e) {
            log.error("알림 전송 실패 (ID 조회 불가): {}", integrationId, e);
        }
    }

    private void sendNoticeInternal(Long authorId, AuthorNoticeSource source, String title, String message, String url) {
        // 1. DB 저장
        AuthorNotice notice = authorNoticeRepository.save(AuthorNotice.builder()
                .authorId(authorId)
                .source(source.name())
                .title(title)
                .message(message)
                .redirectUrl(url)
                .build());

        // 2. 실시간 전송
        if (emitters.containsKey(authorId)) {
            AuthorNoticeDto dto = convertToDto(notice);
            sendToClient(authorId, "author-notice", dto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorNoticeDto> getNotices(String integrationId, boolean onlyUnread) {
        Long authorId = findAuthorIdByIntegrationId(integrationId);

        List<AuthorNotice> list;
        if (onlyUnread) {
            list = authorNoticeRepository.findByAuthorIdAndIsReadFalseOrderByCreatedAtDesc(authorId);
        } else {
            list = authorNoticeRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
        }

        return list.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(String integrationId, Long noticeId) {
        Long authorId = findAuthorIdByIntegrationId(integrationId);

        AuthorNotice notice = authorNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        // 보안 검증: 내 알림이 맞는지 PK로 비교
        if (!notice.getAuthorId().equals(authorId)) {
            throw new SecurityException("본인의 알림만 읽을 수 있습니다.");
        }
        notice.markAsRead();
    }

    @Override
    @Transactional
    public void markAllAsRead(String integrationId) {
        Long authorId = findAuthorIdByIntegrationId(integrationId);
        authorNoticeRepository.markAllAsRead(authorId);
    }

    // --- Private Helper Methods ---

    /**
     * [핵심] IntegrationId(String)를 받아 User 테이블에서 PK(Long)를 찾음
     */
    private Long findAuthorIdByIntegrationId(String integrationId) {
        User user = userRepository.findByIntegrationId(integrationId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + integrationId));
        return user.getId();
    }

    private void sendToClient(Long authorId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(authorId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitters.remove(authorId);
            }
        }
    }

    private AuthorNoticeDto convertToDto(AuthorNotice entity) {
        return AuthorNoticeDto.builder()
                .id(entity.getId())
                .source(AuthorNoticeDto.AuthorNoticeSource.valueOf(entity.getSource()))
                .title(entity.getTitle())
                .message(entity.getMessage())
                .isRead(entity.isRead())
                .createdAt(entity.getCreatedAt())
                .redirectUrl(entity.getRedirectUrl())
                .build();
    }
}