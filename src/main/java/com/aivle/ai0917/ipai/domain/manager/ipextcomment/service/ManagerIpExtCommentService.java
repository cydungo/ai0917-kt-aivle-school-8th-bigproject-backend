package com.aivle.ai0917.ipai.domain.manager.ipextcomment.service;

import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentListResponseDto;
import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentStatusUpdateDto;

public interface ManagerIpExtCommentService {
    // [수정] 반환 타입 변경 (List -> Wrapper DTO)
    ManagerCommentListResponseDto getCommentsByProposal(Long proposalId);

}