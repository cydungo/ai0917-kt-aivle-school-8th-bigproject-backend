package com.aivle.ai0917.ipai.domain.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogDto {
    private Long id;
    private String level; // "INFO", "WARNING", "ERROR"
    private String message;
    private String category; // "DB_BACKUP", "ACCOUNT_CREATED", "API_DELAY", etc.
    private LocalDateTime timestamp;
}
