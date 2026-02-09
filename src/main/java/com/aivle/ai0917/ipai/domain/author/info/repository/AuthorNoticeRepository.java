package com.aivle.ai0917.ipai.domain.author.info.repository;

import com.aivle.ai0917.ipai.domain.author.info.model.AuthorNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorNoticeRepository extends JpaRepository<AuthorNotice, Long> {

    // 내 알림만 최신순 조회
    List<AuthorNotice> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    // 내 읽지 않은 알림 조회
    List<AuthorNotice> findByAuthorIdAndIsReadFalseOrderByCreatedAtDesc(Long authorId);

    // 읽지 않은 알림 개수
    long countByAuthorIdAndIsReadFalse(Long authorId);

    // 내 알림 모두 읽음 처리 (Bulk Update)
    @Modifying
    @Query("UPDATE AuthorNotice n SET n.isRead = true WHERE n.authorId = :authorId AND n.isRead = false")
    void markAllAsRead(@Param("authorId") Long authorId);
}