package com.aivle.ai0917.ipai.domain.manager.ipextcomment.controller;

import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentListResponseDto;
import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentStatusUpdateDto;
import com.aivle.ai0917.ipai.domain.manager.ipextcomment.service.ManagerIpExtCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/manager/ipext/comment")
@RequiredArgsConstructor
public class ManagerIpExtCommentController {

    private final ManagerIpExtCommentService managerIpExtCommentService;

    // 1. [수정] 매니저 - 특정 제안서의 모든 코멘트 조회 (통계 포함)
    // GET /api/v1/manager/ipext/comment/{id} -> 여기서 id는 proposalId
    @GetMapping("/{id}")
    public ResponseEntity<ManagerCommentListResponseDto> getProposalComments(
            @PathVariable("id") Long proposalId) {

        log.info("매니저 IP 확장 코멘트 조회 요청: proposalId={}", proposalId);

        ManagerCommentListResponseDto response = managerIpExtCommentService.getCommentsByProposal(proposalId);

        return ResponseEntity.ok(response);
    }

}