package com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerCommentListResponseDto {
    private Long totalCommentCount;          // 전체 코멘트 개수
    private Integer totalAuthorCount;        // 매칭된 전체 작가 수 (match_author_id 배열 크기)
    private Integer authorCommentCurrentCount; // 실제로 코멘트를 단 작가 수 (중복 제거)
    private List<ManagerCommentResponseDto> comments; // 코멘트 목록
    private List<String> matchedAuthors;
}