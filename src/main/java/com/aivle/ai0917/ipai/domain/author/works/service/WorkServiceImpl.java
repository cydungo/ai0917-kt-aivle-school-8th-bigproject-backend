package com.aivle.ai0917.ipai.domain.author.works.service;

import com.aivle.ai0917.ipai.domain.author.info.dto.AuthorNoticeDto;
import com.aivle.ai0917.ipai.domain.author.info.service.AuthorNoticeService;
import com.aivle.ai0917.ipai.domain.author.works.dto.WorkDto;
import com.aivle.ai0917.ipai.domain.author.works.model.Work;
import com.aivle.ai0917.ipai.domain.author.works.model.WorkStatus; // Import
import com.aivle.ai0917.ipai.domain.author.works.repository.WorkCommandRepository;
import com.aivle.ai0917.ipai.domain.author.works.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final WorkCommandRepository workCommandRepository;
    private final AuthorNoticeService authorNoticeService;

    @Override
    public List<WorkDto.Response> getWorksByAuthor(String authorId, boolean sortByTitle) {
        // ... (이전과 동일, status 필터링 로직은 Repository에서 처리됨)
        List<Work> works;

        if (sortByTitle) {
            // Repository 메서드 이름이 길다면 줄일 수도 있지만, 기존 사용 유지
            works = workRepository.findAllByPrimaryAuthorIdAndStatusNotOrderByTitleAsc(authorId, WorkStatus.DELETED); // DELETED가 Enum에 있다면 사용, 없다면 Repository 쿼리 수정 필요 (아래 Repository 참고)
        } else {
            works = workRepository.findAllByPrimaryAuthorIdAndStatusNotOrderByCreatedAtDesc(authorId, WorkStatus.DELETED);
        }

        return works.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public WorkDto.Response getWorkDetail(Long id) {
        Work work = workRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("작품이 존재하지 않습니다."));
        return convertToResponse(work);
    }

    @Override
    public Long saveWork(WorkDto.CreateRequest dto) {
        // [수정] 기본값으로 WorkStatus.ONGOING.name() 사용

        Long workId = workCommandRepository.insert(
                dto.getUniverseId(),
                dto.getPrimaryAuthorId(),
                dto.getTitle(),
                dto.getSynopsis(),
                dto.getGenre(),
                dto.getCoverImageUrl(),
                WorkStatus.NEW.name()
        );
        // 2. 알림 발송 (작품 생성 완료)
        sendWorkNotice(
                dto.getPrimaryAuthorId(), // String ID 그대로 전달
                "작품 생성 완료",
                "새로운 작품 '" + dto.getTitle() + "'이(가) 성공적으로 생성되었습니다.",
                "/works/" + workId
        );
        return workId;
    }

    @Override
    public void updateStatus(Long id, WorkStatus status) {
        // 1. 상태 업데이트 수행
        workCommandRepository.updateStatus(id, status.name());

        // 2. 알림 발송 (상태 변경)
        workRepository.findById(id).ifPresent(work -> {
            String message = String.format("'%s'의 상태가 %s(으)로 변경되었습니다.",
                    work.getTitle(), status.getDescription());

            sendWorkNotice(
                    work.getPrimaryAuthorId(),
                    "작품 상태 변경",
                    message,
                    "/works/" + id
            );
        });
    }

    @Override
    public void updateWork(Long id, WorkDto.UpdateRequest dto) {
        workCommandRepository.updateWork(
                id,
                dto.getTitle(),
                dto.getSynopsis(),
                dto.getGenre(),
                dto.getCoverImageUrl()
        );
    }

    @Override
    public void deleteWork(Long id) {
        Work work = workRepository.findById(id).orElse(null);
        workCommandRepository.deleteById(id);

        // 2. 알림 발송 (작품 삭제)
        if (work != null) {
            sendWorkNotice(
                    work.getPrimaryAuthorId(),
                    "작품 삭제",
                    "'" + work.getTitle() + "' 작품이 삭제되었습니다.",
                    "/works"
            );
        }
    }


    private void sendWorkNotice(String authorIdStr, String title, String message, String url) {
        try {

            authorNoticeService.sendNotice(
                    authorIdStr,
                    AuthorNoticeDto.AuthorNoticeSource.WORK_PROCESS, // Enum 사용
                    title,
                    message,
                    url
            );
        } catch (NumberFormatException e) {
            log.warn("알림 발송 실패: AuthorID 변환 오류 (ID: {})", authorIdStr);
        } catch (Exception e) {
            log.error("알림 발송 중 예외 발생", e);
        }
    }
    private WorkDto.Response convertToResponse(Work view) {
        return WorkDto.Response.builder()
                .id(view.getId())
                .universeId(view.getUniverseId())
                .primaryAuthorId(view.getPrimaryAuthorId())
                .title(view.getTitle())
                .synopsis(view.getSynopsis())
                .genre(view.getGenre())
                .status(view.getStatus()) // Enum 타입 그대로 설정
                .statusDescription(view.getStatus().getDescription()) // 한글 설명 추가
                .coverImageUrl(view.getCoverImageUrl())
                .createdAt(view.getCreatedAt())
                .build();
    }
}