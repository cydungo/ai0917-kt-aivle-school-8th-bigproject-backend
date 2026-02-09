package com.aivle.ai0917.ipai.domain.author.info.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "author_notices", indexes = {
        @Index(name = "idx_author_notices_author_id", columnList = "authorId"),
        @Index(name = "idx_author_notices_created_at", columnList = "createdAt")
})
public class AuthorNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 핵심: 누구의 알림인가?
    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private String source; // WORK_PROCESS, COMMENT, etc.

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private boolean isRead = false;

    private String redirectUrl; // 클릭 시 이동 경로

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public AuthorNotice(Long authorId, String source, String title, String message, String redirectUrl) {
        this.authorId = authorId;
        this.source = source;
        this.title = title;
        this.message = message;
        this.redirectUrl = redirectUrl;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.isRead = true;
    }
}