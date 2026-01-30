package com.aivle.ai0917.ipai.domain.manager.authors.service;

import com.aivle.ai0917.ipai.domain.manager.authors.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ✅ 매니저가 "본인에게 매칭된 작가"를 조회하기 위한 서비스
 * (Controller -> Service 인터페이스 -> 구현체 -> Repository 구조 유지)
 */
public interface ManagerAuthorQueryService {

    /**
     * 작가 현황 요약
     * - totalAuthors / newAuthors / activeAuthors
     */
    AuthorSummaryResponseDto getSummary(Long managerUserId);

    /**
     * 작가 목록(검색/정렬/페이지)
     */
    Page<AuthorCardResponseDto> getAuthors(Long managerUserId, String keyword, Pageable pageable);

    /**
     * 작가 상세
     */
    AuthorDetailResponseDto getAuthorDetail(Long managerUserId, Long authorId);
}