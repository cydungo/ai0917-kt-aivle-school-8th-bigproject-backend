package com.aivle.ai0917.ipai.domain.user.service;

import com.aivle.ai0917.ipai.domain.user.model.User;
import java.util.Optional;

public interface UserService {
    // 고유 ID로 사용자 찾기
    Optional<User> getUserByIntegrationId(String integrationId);

    // 사용자 등록 (이 과정에서 중복 체크 로직 포함 가능)
    User registerUser(User user);

    boolean existsBySiteEmail(String siteEmail);
}
