package com.aivle.ai0917.ipai.domain.author.analyze.controller;

import com.aivle.ai0917.ipai.domain.author.analyze.dto.AnalyzeRelationshipRequestDto;
import com.aivle.ai0917.ipai.domain.author.analyze.dto.AnalyzeTimelineRequestDto;
import com.aivle.ai0917.ipai.domain.author.analyze.service.AiAnalyzeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ai/author/works/{workId}/analysis")
public class AIAnalyzeController {

    private final AiAnalyzeService aiAnalyzeService;

    /**
     * 1. 인물 관계 분석
     * POST /api/v1/ai/author/works/{workid}/analysis/relationships
     */
    @PostMapping("/relationships")
    public ResponseEntity<?> analyzeRelationships(
            @PathVariable("workId") Long workId,
            @RequestBody AnalyzeRelationshipRequestDto requestDto) {

        log.info("Controller: 인물 관계 분석 요청 workId={}, userId={}", workId, requestDto.getUserId());

        Object result = aiAnalyzeService.analyzeRelationship(
                workId,
                requestDto.getUserId(),
                requestDto.getTarget()
        );

        return ResponseEntity.ok(result);
    }

    /**
     * 2. 타임라인 분석
     * POST /api/v1/ai/author/works/{workid}/analysis/timeline
     */
    @PostMapping("/timeline")
    public ResponseEntity<?> analyzeTimeline(
            @PathVariable("workId") Long workId,
            @RequestBody AnalyzeTimelineRequestDto requestDto) {

        log.info("Controller: 타임라인 분석 요청 workId={}, userId={}", workId, requestDto.getUserId());

        Object result = aiAnalyzeService.analyzeTimeline(
                workId,
                requestDto.getUserId(),
                requestDto.getTarget()
        );

        return ResponseEntity.ok(result);
    }
}