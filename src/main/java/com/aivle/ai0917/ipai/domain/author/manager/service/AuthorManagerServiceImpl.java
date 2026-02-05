package com.aivle.ai0917.ipai.domain.author.manager.service;

import com.aivle.ai0917.ipai.domain.admin.access.model.UserRole;
import com.aivle.ai0917.ipai.domain.author.manager.dto.AuthorManagerResponseDto;
import com.aivle.ai0917.ipai.domain.user.model.User;
import com.aivle.ai0917.ipai.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 작가 -> 자기 매니저 조회 서비스 구현체
 *
 * 동작 흐름:
 * 1) authorUserId(PK)로 users 테이블에서 작가 조회
 * 2) role이 Author인지 확인
 * 3) author.managerIntegrationId 가져오기
 *    - 없으면 "매칭 안됨" 응답
 * 4) userRepository.findByIntegrationId(managerIntegrationId)로 매니저 조회
 * 5) role이 Manager인지 확인 후 DTO로 응답
 */
@Service
@Transactional(readOnly = true)
public class AuthorManagerServiceImpl implements AuthorManagerService {

    private final UserRepository userRepository;

    public AuthorManagerServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthorManagerResponseDto getMyManager(Long authorUserId) {

        // 1) 로그인된 작가(PK)로 조회
        User author = userRepository.findById(authorUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 2) Author만 허용
        if (author.getRole() != UserRole.Author) {
            throw new RuntimeException("작가(Author)만 조회할 수 있습니다.");
        }

        // 3) 매칭된 매니저 integrationId 확인
        String managerIntegrationId = author.getManagerIntegrationId();
        if (managerIntegrationId == null || managerIntegrationId.isBlank()) {
            // 매칭이 아직 없는 상태
            return AuthorManagerResponseDto.builder()
                    .ok(true)
                    .managerIntegrationId(null)
                    .managerName(null)
                    .managerSiteEmail(null)
                    .build();
        }

        // 4) integrationId로 매니저 조회
        User manager = userRepository.findByIntegrationId(managerIntegrationId)
                .orElseThrow(() -> new RuntimeException("매칭된 매니저 정보를 찾을 수 없습니다."));

        // 5) 안전 체크: 실제로 Manager인지 확인
        if (manager.getRole() != UserRole.Manager) {
            throw new RuntimeException("매칭된 계정이 매니저(Manager)가 아닙니다.");
        }

        return AuthorManagerResponseDto.builder()
                .ok(true)
                .managerIntegrationId(manager.getIntegrationId())
                .managerName(manager.getName())
                .managerSiteEmail(manager.getSiteEmail())
                .build();
    }


    @Override
    @Transactional
    public void deleteMyManager(Long authorUserId) {

        User author = userRepository.findById(authorUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (author.getRole() != UserRole.Author) {
            throw new RuntimeException("작가(Author)만 삭제할 수 있습니다.");
        }

        if (author.getManagerIntegrationId() == null || author.getManagerIntegrationId().isBlank()) {
            return;
        }

        author.setManagerIntegrationId(null);
        userRepository.save(author);
    }
}
