package com.aivle.ai0917.ipai.domain.author.works.service;

import com.aivle.ai0917.ipai.domain.author.works.dto.WorkDto;
import com.aivle.ai0917.ipai.domain.author.works.model.Work;
import com.aivle.ai0917.ipai.domain.author.works.repository.WorkCommandRepository;
import com.aivle.ai0917.ipai.domain.author.works.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final WorkCommandRepository workCommandRepository;

    @Override
    public List<WorkDto.Response> getWorksByAuthor(String authorId, boolean sortByTitle) {
        List<Work> works;

        if (sortByTitle) {
            works = workRepository
                    .findAllByPrimaryAuthorIdAndStatusNotOrderByTitleAsc(authorId, "DELETED");
        } else {
            works = workRepository
                    .findAllByPrimaryAuthorIdAndStatusNotOrderByCreatedAtDesc(authorId, "DELETED");
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
        return workCommandRepository.insert(
                dto.getUniverseId(),
                dto.getPrimaryAuthorId(),
                dto.getTitle(),
                dto.getSynopsis(),
                dto.getGenre(),
                dto.getCoverImageUrl()
        );
    }

    @Override
    public void updateStatus(Long id, String status) {
        workCommandRepository.updateStatus(id, status);
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
        workCommandRepository.deleteById(id); // 영구 삭제
    }

    private WorkDto.Response convertToResponse(Work view) {
        return WorkDto.Response.builder()
                .id(view.getId())
                .universeId(view.getUniverseId())
                .primaryAuthorId(view.getPrimaryAuthorId())
                .title(view.getTitle())
                .synopsis(view.getSynopsis())
                .genre(view.getGenre())
                .status(view.getStatus())
                .coverImageUrl(view.getCoverImageUrl())
                .createdAt(view.getCreatedAt())
                .build();
    }
}
