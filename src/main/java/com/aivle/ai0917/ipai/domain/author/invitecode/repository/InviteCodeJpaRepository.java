/**
 package com.aivle.ai0917.ipai.domain.author.invitecode.repository;


 import org.springframework.stereotype.Repository;

 import java.time.Instant;
 import java.util.Map;
 import java.util.Optional;
 import java.util.concurrent.ConcurrentHashMap;

 @Repository
 public class InviteCodeRepository {

 // code -> (authorIntegrationId, expiresAt)
 private final Map<String, CodeEntry> codeStore = new ConcurrentHashMap<>();

 // authorIntegrationId -> code (재발급 시 기존 코드 무효화)
 private final Map<String, String> authorToCode = new ConcurrentHashMap<>();

 public void save(String code, String authorIntegrationId, Instant expiresAt) {
 codeStore.put(code, new CodeEntry(authorIntegrationId, expiresAt));
 authorToCode.put(authorIntegrationId, code);
 }

 public Optional<CodeEntry> findByCode(String code) {
 return Optional.ofNullable(codeStore.get(code));
 }

 public boolean existsByCode(String code) {
 return codeStore.containsKey(code);
 }


 public void deleteByCode(String code) {
 CodeEntry entry = codeStore.remove(code);
 if (entry != null) {
 authorToCode.remove(entry.authorIntegrationId(), code);
 }
 }

 public void deleteOldCodeOfAuthor(String authorIntegrationId) {
 String old = authorToCode.get(authorIntegrationId);
 if (old != null) {
 deleteByCode(old);
 }
 }

 public record CodeEntry(String authorIntegrationId, Instant expiresAt) {}
 }
 **/

package com.aivle.ai0917.ipai.domain.author.invitecode.repository;

import com.aivle.ai0917.ipai.domain.author.invitecode.model.InviteCode;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface InviteCodeJpaRepository extends JpaRepository<InviteCode, Long> {

    Optional<InviteCode> findByCode(String code);

    Optional<InviteCode> findTopByAuthorIntegrationIdAndUsedAtIsNullOrderByCreatedAtDesc(String authorIntegrationId);

    boolean existsByCode(String code);

    /**
     * ✅ 원자적으로 "유효한 코드"를 사용 처리 (동시에 여러 명이 입력해도 1명만 성공)
     * 성공: 1, 실패: 0
     */
    @Modifying
    @Query("""
        UPDATE InviteCode ic
           SET ic.usedAt = :now
         WHERE ic.code = :code
           AND ic.usedAt IS NULL
           AND ic.expiresAt > :now
    """)
    int consumeIfValid(@Param("code") String code, @Param("now") LocalDateTime now);
}

