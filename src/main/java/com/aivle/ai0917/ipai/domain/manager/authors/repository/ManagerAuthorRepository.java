package com.aivle.ai0917.ipai.domain.manager.authors.repository;

import com.aivle.ai0917.ipai.domain.admin.access.model.UserRole;
import com.aivle.ai0917.ipai.domain.user.model.User;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * ✅ "매니저 전용 작가 조회" 레포지토리
 * 핵심:
 * - 매니저가 조회할 때는 항상 "managerIntegrationId = 현재 매니저 integrationId" 조건으로 제한하여
 *   "내가 매칭한 작가만" 나오도록 보장한다.
 */
public interface ManagerAuthorRepository extends JpaRepository<User, Long> {

    /**
     * ✅ 기본 목록 조회 (name/createdAt 등 일반 정렬은 Pageable.sort로 처리 가능)
     * - role=Author
     * - managerIntegrationId = :managerIntegrationId (내 매칭 작가만)
     * - keyword가 있으면 name/siteEmail에 LIKE 검색
     */
    @Query("""
        SELECT u
        FROM User u
        WHERE u.role = :role
          AND u.managerIntegrationId = :managerIntegrationId
          AND (:keyword IS NULL OR :keyword = '' OR u.name LIKE %:keyword% OR u.siteEmail LIKE %:keyword%)
    """)
    Page<User> findMatchedAuthors(@Param("managerIntegrationId") String managerIntegrationId,
                                  @Param("keyword") String keyword,
                                  @Param("role") UserRole role,
                                  Pageable pageable);

    /**
     * ✅ 작품 수(workCount) 기준 정렬이 필요할 때(Desc)
     * - User와 Work는 FK로 직접 연결이 없으므로
     *   Work.primaryAuthorId = User.integrationId 로 조인
     * - GROUP BY u 후 COUNT(w)로 작품 수 정렬
     */
    @Query(
            value = """
            SELECT u
            FROM User u
            LEFT JOIN com.aivle.ai0917.ipai.domain.author.works.model.Work w
              ON w.primaryAuthorId = u.integrationId
            WHERE u.role = :role
              AND u.managerIntegrationId = :managerIntegrationId
              AND (:keyword IS NULL OR :keyword = '' OR u.name LIKE %:keyword% OR u.siteEmail LIKE %:keyword%)
            GROUP BY u
            ORDER BY COUNT(w) DESC
        """,
            countQuery = """
            SELECT COUNT(u)
            FROM User u
            WHERE u.role = :role
              AND u.managerIntegrationId = :managerIntegrationId
              AND (:keyword IS NULL OR :keyword = '' OR u.name LIKE %:keyword% OR u.siteEmail LIKE %:keyword%)
        """
    )
    Page<User> findMatchedAuthorsOrderByWorkCountDesc(@Param("managerIntegrationId") String managerIntegrationId,
                                                      @Param("keyword") String keyword,
                                                      @Param("role") UserRole role,
                                                      Pageable pageable);

    /**
     * ✅ 작품 수(workCount) 기준 정렬(Asc)
     */
    @Query(
            value = """
            SELECT u
            FROM User u
            LEFT JOIN com.aivle.ai0917.ipai.domain.author.works.model.Work w
              ON w.primaryAuthorId = u.integrationId
            WHERE u.role = :role
              AND u.managerIntegrationId = :managerIntegrationId
              AND (:keyword IS NULL OR :keyword = '' OR u.name LIKE %:keyword% OR u.siteEmail LIKE %:keyword%)
            GROUP BY u
            ORDER BY COUNT(w) ASC
        """,
            countQuery = """
            SELECT COUNT(u)
            FROM User u
            WHERE u.role = :role
              AND u.managerIntegrationId = :managerIntegrationId
              AND (:keyword IS NULL OR :keyword = '' OR u.name LIKE %:keyword% OR u.siteEmail LIKE %:keyword%)
        """
    )
    Page<User> findMatchedAuthorsOrderByWorkCountAsc(@Param("managerIntegrationId") String managerIntegrationId,
                                                     @Param("keyword") String keyword,
                                                     @Param("role") UserRole role,
                                                     Pageable pageable);

    /**
     * ✅ 요약용: 내 매칭 작가 총합
     */
    long countByRoleAndManagerIntegrationId(UserRole role, String managerIntegrationId);

    /**
     * ✅ 요약용: 내 매칭 작가 중 최근 7일 이내 가입한 작가 수(= createdAt 기준)
     */
    long countByRoleAndManagerIntegrationIdAndCreatedAtGreaterThanEqual(UserRole role,
                                                                        String managerIntegrationId,
                                                                        LocalDateTime since);

    /**
     * ✅ 요약용: 내 매칭 작가 중 최근 1시간 이내 활동한 작가 수(= lastActivityAt 기준)
     */
    long countByRoleAndManagerIntegrationIdAndLastActivityAtGreaterThanEqual(UserRole role,
                                                                             String managerIntegrationId,
                                                                             LocalDateTime since);

    /**
     * ✅ 상세 조회도 보안상 "내 매칭 작가만" 조회되도록 제한
     * - authorId(users PK)
     * - managerIntegrationId = 내 매니저 integrationId
     */
    @Query("""
        SELECT u
        FROM User u
        WHERE u.id = :authorId
          AND u.role = :role
          AND u.managerIntegrationId = :managerIntegrationId
    """)
    Optional<User> findMatchedAuthorDetail(@Param("authorId") Long authorId,
                                           @Param("managerIntegrationId") String managerIntegrationId,
                                           @Param("role") UserRole role);
}
