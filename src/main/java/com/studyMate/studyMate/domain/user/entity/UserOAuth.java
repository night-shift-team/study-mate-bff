package com.studyMate.studyMate.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyMate.studyMate.domain.user.data.OAuthType;
import com.studyMate.studyMate.global.data.BaseEntity;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users_oauth")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "removed_dt IS NULL")
@SuperBuilder
public class UserOAuth extends BaseEntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_oauth_id")
    private String userOAuthId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "oauth_type")
    private OAuthType oauthType;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "removed_dt")
    @Schema(description = "삭제일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime removedDt;
}
