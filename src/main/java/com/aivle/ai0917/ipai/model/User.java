package com.aivle.ai0917.ipai.model;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * 우리 서비스 사용자 엔티티
 *
 * - naverId: 네이버가 제공하는 고유 사용자 ID (절대 변하지 않는 값이라 식별자로 사용)
 * - 이메일/이름/성별/생일/출생년도/휴대폰: 네이버 동의 항목에 따라 null일 수 있음
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_users_naver_id", columnList = "naverId", unique = true)
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 네이버 고유 사용자 ID (response.id) - PK로 쓰기 좋은 값 */
    @Column(nullable = false, unique = true, length = 64)
    private String naverId;

    private String email;
    private String name;

    /** "M" or "F" */
    private String gender;

    /** "YYYY" */
    private String birthYear;

    /** "MM-DD" */
    private String birthday;

    /** "010-xxxx-xxxx" */
    private String mobile;

    /** Spring Security 권한 형태 */
    private String role = "ROLE_USER";

    private Instant createdAt = Instant.now();

    protected User() {}

    public User(String naverId, String email, String name, String gender, String birthYear, String birthday, String mobile) {
        this.naverId = naverId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.birthYear = birthYear;
        this.birthday = birthday;
        this.mobile = mobile;
        this.role = "ROLE_USER";
        this.createdAt = Instant.now();
    }

    // --- getter / setter ---
    public Long getId() { return id; }
    public String getNaverId() { return naverId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public String getBirthYear() { return birthYear; }
    public String getBirthday() { return birthday; }
    public String getMobile() { return mobile; }
    public String getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }

    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBirthYear(String birthYear) { this.birthYear = birthYear; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
