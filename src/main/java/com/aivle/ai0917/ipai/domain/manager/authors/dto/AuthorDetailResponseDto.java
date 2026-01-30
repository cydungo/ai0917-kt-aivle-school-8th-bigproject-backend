package com.aivle.ai0917.ipai.domain.manager.authors.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * GET /api/v1/manager/authors/{id} 응답 DTO
 * - 유저 테이블 기반 상세 정보 + 최근 작품 5개
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDetailResponseDto {
    private Long id;
    private String name;

    // 프론트 코드가 email로 쓰고 있어서 siteEmail을 내려줌
    private String email;

    // 요구사항: 성별/출생연도/생일
    private String gender;
    private String birthYear;
    private String birthday;

    private LocalDateTime createdAt;

    // 프론트가 lastLogin(string)을 쓰고 있어서 lastActivityAt을 문자열로 내려줌
    private String lastLogin;

    // 최근 작품(최신 5개)
    private List<WorkSummaryDto> recentWorks;
}