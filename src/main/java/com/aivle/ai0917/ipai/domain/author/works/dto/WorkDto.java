package com.aivle.ai0917.ipai.domain.author.works.dto;

import lombok.*;
import java.time.LocalDateTime;

public class WorkDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CreateRequest {
        private String title;
        private String synopsis;
        private String genre;
        private String coverImageUrl;
        private String primaryAuthorId; // 작성자 ID
        private Long universeId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String synopsis;
        private String genre;
        private String coverImageUrl;
    }

    @Getter
    @Builder
    public static class Response {
        private Long id;
        private Long universeId;
        private String primaryAuthorId;
        private String title;
        private String synopsis;
        private String genre;
        private String status;
        private String coverImageUrl;
        private LocalDateTime createdAt;
    }
}