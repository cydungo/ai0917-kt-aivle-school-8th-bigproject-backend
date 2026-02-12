package com.aivle.ai0917.ipai.domain.manager.ipextcomment.service;

import com.aivle.ai0917.ipai.domain.author.ipextcomment.model.IpProposalComment;
import com.aivle.ai0917.ipai.domain.author.ipextcomment.repository.IpProposalCommentRepository;
import com.aivle.ai0917.ipai.domain.manager.authors.repository.ManagerAuthorRepository;
import com.aivle.ai0917.ipai.domain.manager.ipext.model.IpProposal;
import com.aivle.ai0917.ipai.domain.manager.ipext.repository.IpProposalRepository;
import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentListResponseDto;
import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentResponseDto;
import com.aivle.ai0917.ipai.domain.manager.ipextcomment.dto.ManagerCommentStatusUpdateDto;
import com.aivle.ai0917.ipai.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerIpExtCommentServiceImpl implements ManagerIpExtCommentService {

    private final IpProposalCommentRepository ipProposalCommentRepository;
    private final IpProposalRepository ipProposalRepository;
    private final ManagerAuthorRepository managerAuthorRepository; // 작가 정보 조회용

    @Override
    public ManagerCommentListResponseDto getCommentsByProposal(Long proposalId) {

        // 1. IpProposal 조회
        IpProposal proposal = ipProposalRepository.findById(proposalId)
                .orElseThrow(() -> new NoSuchElementException("해당 제안서를 찾을 수 없습니다. ID: " + proposalId));

        // 2. 매칭된 작가 ID 목록 가져오기
        List<String> matchAuthorIds = proposal.getMatchAuthorIds();

        // 3. [수정됨] 매칭된 작가들의 "이름"만 조회하여 리스트로 변환
        List<String> matchedAuthorNames;

        if (matchAuthorIds != null && !matchAuthorIds.isEmpty()) {
            // User 엔티티 조회 (Repository에 findByIntegrationIdIn 메서드 필요)
            List<User> authors = managerAuthorRepository.findByIntegrationIdIn(matchAuthorIds);

            // User 객체에서 이름(Name)만 추출
            matchedAuthorNames = authors.stream()
                    .map(User::getName)
                    .collect(Collectors.toList());
        } else {
            matchedAuthorNames = Collections.emptyList();
        }

        // 매칭된 작가 수 (이름 리스트 크기 활용)
        int totalMatchAuthorCount = matchedAuthorNames.size();

        // 4. 해당 제안서의 전체 코멘트 조회
        List<IpProposalComment> comments = ipProposalCommentRepository.findAllByIpProposalId(proposalId);

        // 5. 활성화된 코멘트 필터링 (ARCHIVED 제외)
        List<IpProposalComment> activeComments = comments.stream()
                .filter(comment -> comment.getStatus() != IpProposalComment.Status.ARCHIVED)
                .collect(Collectors.toList());

        // 6. 실제로 코멘트를 단 작가 수 계산 (중복 제거)
        int actualParticipatingAuthorCount = (int) activeComments.stream()
                .map(IpProposalComment::getAuthorId)
                .distinct()
                .count();

        // 7. DTO 리스트 변환
        List<ManagerCommentResponseDto> commentDtos = activeComments.stream()
                .map(ManagerCommentResponseDto::new)
                .collect(Collectors.toList());

        // 8. 최종 결과 반환
        return ManagerCommentListResponseDto.builder()
                .totalCommentCount((long) activeComments.size())
                .totalAuthorCount(totalMatchAuthorCount)
                .authorCommentCurrentCount(actualParticipatingAuthorCount)
                .matchedAuthors(matchedAuthorNames) // [수정] 이름 리스트 주입
                .comments(commentDtos)
                .build();
    }


}