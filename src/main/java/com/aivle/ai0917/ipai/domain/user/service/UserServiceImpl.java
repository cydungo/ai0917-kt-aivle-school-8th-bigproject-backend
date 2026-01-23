package com.aivle.ai0917.ipai.domain.user.service;

import com.aivle.ai0917.ipai.domain.user.model.User;
import com.aivle.ai0917.ipai.domain.user.repository.UserRepository;
import com.aivle.ai0917.ipai.global.utils.Base62Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserByIntegrationId(String integrationId) {
        return userRepository.findByIntegrationId(integrationId);
    }

    // ✅ 추가: 리포지토리의 existsBySiteEmail 호출
    @Override
    public boolean existsBySiteEmail(String siteEmail) {
        return userRepository.existsBySiteEmail(siteEmail);
    }

    @Override
    @Transactional
    public User registerUser(User user) {
        // 8자리 ID 수동 할당 로직 (중복 방지 강화 버전)
        String uniqueId = Base62Util.generate8CharId();
        while (userRepository.findByIntegrationId(uniqueId).isPresent()) {
            uniqueId = Base62Util.generate8CharId();
        }
        user.setIntegrationId(uniqueId);

        return userRepository.save(user);
    }
}