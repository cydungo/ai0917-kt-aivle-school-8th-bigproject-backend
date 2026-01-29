package com.aivle.ai0917.ipai.domain.author.works.repository;

import com.aivle.ai0917.ipai.domain.author.works.model.Work;
import com.aivle.ai0917.ipai.domain.author.works.model.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> { // ID 타입을 Long으로 변경

    // List로 반환하여 중복 결과 에러 방지
    List<Work> findByUserIntegrationId(String userIntegrationId);

    // userIntegrationId로 삭제
    void deleteByUserIntegrationId(String userIntegrationId);

    //특정 작가의 전체 작품 수
    long countByUserIntegrationId(String userIntegratoinId);

    // 특정 작가의 최근 작품 5개
    List<Work> findTop5ByUserIntegrationIdOrderByCreatedAtDesc(String userIntegrationId);

}