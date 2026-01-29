package com.aivle.ai0917.ipai.domain.manager.authors.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 작가 상세 정보에서 recentWorks 리스트의 원소 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkSummaryDto {
    private Long id;
    private String title;
    private LocalDateTime createdAt;
}