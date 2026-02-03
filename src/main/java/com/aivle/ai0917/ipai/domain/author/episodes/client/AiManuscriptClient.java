package com.aivle.ai0917.ipai.domain.author.episodes.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AiManuscriptClient {

    private final WebClient aiWebClient;
    private final ObjectMapper objectMapper; // [추가] JSON 파싱을 위한 도구

    /**
     * AI 서버로 원문 저장 (Storage1 원문 저장)
     */
    public String saveNovelToAi(Long episodeId, String userId, Long workId, Integer epNum, String txt) {
        AiNovelSaveRequest request = AiNovelSaveRequest.builder()
                .userId(userId)
                .workId(workId)
                .epNum(epNum)
                .txt(txt)
                .build();

        log.info("AI 서버로 원문 저장 요청: EpisodeId={}, WorkId={}, Ep={}", episodeId, workId, epNum);

        try {
            // 저장 응답은 보통 경로만 오거나 간단한 JSON이므로 String으로 받아도 무방하지만,
            // 필요하다면 여기서도 파싱할 수 있습니다. 현재는 유지합니다.
            String response = aiWebClient.post()
                    .uri("/novel_save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            log.info("AI 서버 원문 저장 완료: {}", response);
            return response;

        } catch (Exception e) {
            log.error("AI 서버 원문 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("원문 저장 실패: " + e.getMessage());
        }
    }

    /**
     * AI 서버에서 원문 읽기 (Storage1 원문 읽기)
     * [핵심 수정] 받아온 데이터가 JSON이라면 파싱해서 '순수 텍스트'만 반환
     */
    public String readNovelFromAi(String userId, Long workId, Integer epNum) {
        log.info("AI 서버에서 원문 읽기 요청: WorkId={}, Ep={}", workId, epNum);

        try {
            // 1. 응답을 String으로 받음 (이때 "{\"txt\": \"내용\"}" 형태일 수 있음)
            String responseBody = aiWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/novel_read")
                            .queryParam("user_id", userId)
                            .queryParam("work_id", workId)
                            .queryParam("ep_num", epNum)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            // 2. JSON 파싱 시도 (포장지 벗기기)
            if (responseBody != null) {
                try {
                    JsonNode rootNode = objectMapper.readTree(responseBody);

                    // Case A: {"txt": "..."} 또는 {"text": "..."} 형태인 경우
                    if (rootNode.has("txt")) {
                        return rootNode.get("txt").asText();
                    } else if (rootNode.has("text")) {
                        return rootNode.get("text").asText();
                    }
                    // Case B: 단순히 "내용..." 형태의 JSON 문자열인 경우
                    else if (rootNode.isTextual()) {
                        return rootNode.asText();
                    }
                    // Case C: JSON 객체도 아니고 따옴표로 감싸진 문자열도 아닌 경우 (그냥 평문)
                    else {
                        // rootNode.toString()을 하면 따옴표가 다시 붙을 수 있으니 responseBody 반환 고려
                        // 하지만 일반적인 텍스트라면 asText()가 안전함
                        return rootNode.asText();
                    }
                } catch (Exception e) {
                    // JSON이 아니라 진짜 평문(Plain Text)이어서 파싱 에러가 났다면 원본 그대로 반환
                    return responseBody;
                }
            }

            return responseBody;

        } catch (Exception e) {
            log.error("AI 서버 원문 읽기 실패: {}", e.getMessage(), e);
            throw new RuntimeException("원문 읽기 실패: " + e.getMessage());
        }
    }

    @Getter
    @Builder
    private static class AiNovelSaveRequest {
        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("work_id")
        private Long workId;

        @JsonProperty("ep_num")
        private Integer epNum;

        @JsonProperty("txt")
        private String txt;
    }
}