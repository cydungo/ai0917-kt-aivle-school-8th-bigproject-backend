package com.aivle.ai0917.ipai.domain.admin.dashboard.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

// 일일 활성 사용자 통계 엔티티
@Entity
@Table(name = "daily_active_users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyActiveUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDateTime date;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}