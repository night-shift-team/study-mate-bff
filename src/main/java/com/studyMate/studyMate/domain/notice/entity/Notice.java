package com.studyMate.studyMate.domain.notice.entity;

import com.studyMate.studyMate.domain.notice.data.NoticeCategory;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Notice extends BaseEntityDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Column
    private String title;
    private String content;
    private String purpose;

    @Enumerated(EnumType.STRING)
    private NoticeCategory noticeType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User publisher;

    @Column(name = "display_start_time", nullable = false)
    private LocalDateTime displayStartTime;
    @Column(name = "display_end_time", nullable = false)
    private LocalDateTime displayEndTime;

    @Column(name = "maintenance_start_time", nullable = true)
    private LocalDateTime maintenanceStartTime = null;
    @Column(name = "maintenance_end_time", nullable = true)
    private LocalDateTime maintenanceEndTime = null;

    @PrePersist
    @PreUpdate
    private void validatePublisherRole() {
        // User Role 7 이상만 Notice 작성이 가능 (운영자 레벨)
        if (publisher.getRole() < 7) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
