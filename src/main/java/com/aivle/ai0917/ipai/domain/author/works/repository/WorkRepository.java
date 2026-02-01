package com.aivle.ai0917.ipai.domain.author.works.repository;

import com.aivle.ai0917.ipai.domain.author.works.model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {

    List<Work> findAllByPrimaryAuthorIdAndStatusNotOrderByCreatedAtDesc(
            String primaryAuthorId, String status
    );

    List<Work> findAllByPrimaryAuthorIdAndStatusNotOrderByTitleAsc(
            String primaryAuthorId, String status
    );
}
