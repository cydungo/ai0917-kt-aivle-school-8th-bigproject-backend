package com.aivle.ai0917.ipai.domain.author.info.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class AuthorNoticeDto {

    private Long id;
    private AuthorNoticeSource source; // 작가 전용 소스
    private String title;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private String redirectUrl; // 클릭 시 이동할 페이지 (예: 내 작품 상세)

    /**
     * 작가용 알림 소스 정의
     */
    public enum AuthorNoticeSource {
        WORK_PROCESS("작품 처리"),   // 예: 작품 생성 완료, 변환 성공/실패
        COMMENT("댓글 알림"),       // 예: 내 작품에 댓글 달림
        SYSTEM_NOTICE("공지사항"),  // 전체 작가 공지
        INVITE("초대 알림");        // 협업 초대 등

        private final String description;

        AuthorNoticeSource(String description) {
            this.description = description;
        }
    }
}