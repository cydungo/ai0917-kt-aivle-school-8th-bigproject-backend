package com.aivle.ai0917.ipai.domain.manager.dashboard.service;

import com.aivle.ai0917.ipai.domain.admin.access.model.UserRole;
import com.aivle.ai0917.ipai.domain.manager.authors.repository.ManagerAuthorRepository;
import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardNoticeDto;
import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardPageResponseDto;
import com.aivle.ai0917.ipai.domain.manager.dashboard.dto.ManagerDashboardSummaryResponseDto;
import com.aivle.ai0917.ipai.domain.manager.ipext.repository.IpProposalRepository;
import com.aivle.ai0917.ipai.domain.notice.model.Notice;
import com.aivle.ai0917.ipai.domain.notice.repository.NoticeRepository;
import com.aivle.ai0917.ipai.domain.user.model.User;
import com.aivle.ai0917.ipai.domain.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ManagerDashboardServiceImpl implements ManagerDashboardService {

    private static final int DEFAULT_NOTICE_LIMIT = 5;

    private final UserRepository userRepository;
    private final ManagerAuthorRepository managerAuthorRepository;
    private final NoticeRepository noticeRepository;
    private final IpProposalRepository ipProposalRepository;

    @Override
    public ManagerDashboardPageResponseDto getDashboardPage(Long managerUserId) {
        return ManagerDashboardPageResponseDto.builder()
                .summary(getDashboardSummary(managerUserId))
                .notices(getRecentNotices(DEFAULT_NOTICE_LIMIT))
                .build();
    }

    @Override
    public ManagerDashboardSummaryResponseDto getDashboardSummary(Long managerUserId) {
        User manager = userRepository.findById(managerUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (manager.getRole() != UserRole.Manager) {
            throw new RuntimeException("매니저(Manager)만 조회할 수 있습니다.");
        }

        String managerIntegrationId = manager.getIntegrationId();

        long managedAuthors = managerAuthorRepository.countByRoleAndManagerIntegrationId(
                UserRole.Author, managerIntegrationId
        );

        long activeAuthors = managerAuthorRepository.countByRoleAndManagerIntegrationIdAndLastActivityAtGreaterThanEqual(
                UserRole.Author, managerIntegrationId, LocalDateTime.now().minusHours(1)
        );


        long pendingProposals = ipProposalRepository.countByManagerIdAndStatusAndFileSizeIsNotNull(
                managerIntegrationId,
                "PENDING_APPROVAL"
        );

        return ManagerDashboardSummaryResponseDto.builder()
                .pendingProposals(pendingProposals) // 수정된 값 주입
                .managedAuthors(managedAuthors)
                .activeAuthors(activeAuthors)
                .build();

    }



    private List<ManagerDashboardNoticeDto> getRecentNotices(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));

        return noticeRepository.findAll(pageable)
                .stream()
                .map(this::mapNotice)
                .toList();
    }

    private ManagerDashboardNoticeDto mapNotice(Notice notice) {
        return ManagerDashboardNoticeDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .writer(notice.getWriter())
                .createdAt(notice.getCreatedAt())
                .build();
    }
}