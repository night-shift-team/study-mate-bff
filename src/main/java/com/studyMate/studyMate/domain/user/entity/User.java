package com.studyMate.studyMate.domain.user.entity;

import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Tsid
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "login_pw", nullable = false)
    private String loginPw;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "init_score", nullable = false)
    private Integer initScore;

    @Column(name = "role", nullable = false)
    private Integer role;

    @Column(name = "created_dt", updatable = false)
    private LocalDateTime createdDt;

    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;

    @Column(name = "removed_dt")
    private LocalDateTime removedDt;

    public void setNewPassword(String password) {
        this.loginPw = password;
    }

    public void setNewProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setNewNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
