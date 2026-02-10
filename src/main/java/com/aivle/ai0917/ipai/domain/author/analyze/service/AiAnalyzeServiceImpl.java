package com.aivle.ai0917.ipai.domain.author.analyze.service;

import com.aivle.ai0917.ipai.domain.author.analyze.client.AiGraphClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiAnalyzeServiceImpl implements AiAnalyzeService {

    private final AiGraphClient aiGraphClient;

    @Override
    public Object analyzeRelationship(Long workId, String userId, String target) {
        log.info("ServiceImpl: 인물 관계 분석 실행 workId={}, userId={}", workId, userId);
        return aiGraphClient.requestRelationshipAnalysis(workId, userId, target);
    }

    @Override
    public Object analyzeTimeline(Long workId, String userId, List<Integer> targetList) {
        log.info("ServiceImpl: 타임라인 분석 실행 workId={}, userId={}, targetSize={}",
                workId, userId, (targetList != null ? targetList.size() : 0));
        return aiGraphClient.requestTimelineAnalysis(workId, userId, targetList);
    }
}