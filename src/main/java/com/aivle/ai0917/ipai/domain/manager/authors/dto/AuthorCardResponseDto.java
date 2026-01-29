package com.aivle.ai0917.ipai.domain.manager.authors.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * GET /api/v1/manager/authors(또는 /list) 응답 목록(카드/테이블) 1개 단위 DTO
 * 프론트(ManagerAuthorManagement.tsx)에서 사용하는 필드에 맞춤
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCardResponseDto {
    private Long id;                 // users PK (상세 조회할 때 사용)
    private String name;             // 작가명
    private String email;            // 프론트가 email로 쓰므로 siteEmail을 내려줌
    private long workCount;          // works.user_integration_id 기준 카운트
    private LocalDateTime createdAt; // 가입일
    private String status;           // ACTIVE / INACTIVE (lastActivityAt 기준)
}