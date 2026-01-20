package com.aivle.ai0917.ipai.domain.admin.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
// DAU 데이터
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DauResponseDto {
    private Integer today;
    private Integer yesterday;
    private Double sevenDayAverage;
    private List<DailyDauDto> dailyData;

}

