package com.aivle.ai0917.ipai.domain.manager.authors.dto;

import lombok.*;
/**
 * GET /api/v1/manager/authors/summary 응답 DTO
 * - totalAuthors: 내(매니저)에게 매칭된 전체 작가 수
 * - newAuthors: 최근 7일 이내 가입한 작가 수
 * - activeAuthors: lastActivityAt이 최근 1시간 이내인 작가 수
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorSummaryResponseDto {
    private long totalAuthors;
    private long newAuthors;
    private long activeAuthors;
}
