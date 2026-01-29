package com.aivle.ai0917.ipai.domain.author.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 작가가 자신의 매니저 정보를 조회할 때 내려주는 DTO
 * - managerIntegrationId: 매니저의 외부 연동 ID(8자리)
 * - name/siteEmail: 화면 표시용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorManagerResponseDto {
    private boolean ok;
    private String managerIntegrationId;
    private String managerName;
    private String managerSiteEmail;
}