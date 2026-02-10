package com.aivle.ai0917.ipai.domain.author.analyze.service;

import java.util.List;

public interface AiAnalyzeService {

    /**
     * 인물 관계 분석
     */
    Object analyzeRelationship(Long workId, String userId, String target);

    /**
     * 타임라인 분석
     */
    Object analyzeTimeline(Long workId, String userId, List<Integer> targetList);
}