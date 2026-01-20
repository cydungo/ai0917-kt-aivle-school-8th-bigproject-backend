package com.aivle.ai0917.ipai.domain.admin.dashboard.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


// 배포 정보 엔티티
@Entity
@Table(name = "deployment_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeploymentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String version;

    @Column(nullable = false, length = 20)
    private String environment;

    @Column(nullable = false)
    private LocalDateTime deploymentTime;

    @Column(nullable = false)
    private LocalDateTime serverStartTime;

    @Column
    private String deployedBy;

    @Column(columnDefinition = "TEXT")
    private String releaseNotes;
}
