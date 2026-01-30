package com.aivle.ai0917.ipai.domain.author.works.service;

import com.aivle.ai0917.ipai.domain.author.works.dto.WorkDto;
import java.util.List;

public interface WorkService {
    List<WorkDto.Response> getWorksByAuthor(String authorId, boolean sortByTitle); // sort 추가
    WorkDto.Response getWorkDetail(Long id);
    Long saveWork(WorkDto.CreateRequest request);
    void updateStatus(Long id, String status);
    void updateWork(Long id, WorkDto.UpdateRequest request); // 누락된 메서드 추가
    void deleteWork(Long id);
}