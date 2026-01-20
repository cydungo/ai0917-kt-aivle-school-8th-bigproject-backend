package com.aivle.ai0917.ipai.domain.admin.dashboard.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

// 시스템 로그 목록
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogsResponseDto {
    private List<SystemLogDto> logs;
    private Integer totalCount;
}


