package com.aivle.ai0917.ipai.domain.author.manager.service;

import com.aivle.ai0917.ipai.domain.author.manager.dto.AuthorManagerResponseDto;

public interface AuthorManagerService {

    /**
     * 로그인된 작가가 "나의 매니저"를 조회
     */
    AuthorManagerResponseDto getMyManager(Long authorUserId);

    /**
     * 로그인된 작가가 "나의 매니저" 매칭을 삭제
     */
    void deleteMyManager(Long authorUserId);


}
