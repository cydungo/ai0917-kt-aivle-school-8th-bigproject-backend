package com.aivle.ai0917.ipai.domain.manager.ipext.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IpProposalStatusUpdateDto {
    private String status; // "APPROVED" or "REJECTED"
}