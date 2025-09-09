package com.studyMate.studyMate.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.global.data.BaseEntityDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private String userId;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "profile_img")
    private String profileImg;

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;

    @Column(name = "local_login_pw", nullable = false)
    private String localLoginPw;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private UserStatus status;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "init_score", nullable = false)
    private Integer initScore;

    @Column(name = "role", nullable = false)
    private Integer role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserOAuth> userOAuths = new ArrayList<>();

    @Column(name = "removed_dt")
    @Schema(description = "삭제일", example = "2024-09-28T16:23:00.00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime removedDt;


    public void setNewProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setNewNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRole(int role) {
        this.role = role;
    }

    /**
     * 일반적으로 점수 누적시 사용
     */
    public int accumulateUserScore(int score) {
        return accumulateUserScore(score, 1);
    }

    /**
     * Level Test, Contest 등 점수 가중치가 필요할 때 사용
     * @param weight 가중치 배수 (default = 1)
     */
    public int accumulateUserScore(int score, int weight) {
        int MAX_SCORE = 512000;
        int MIN_SCORE = 1000;

        int weightedScore = score * weight;

        if(this.score + weightedScore > MAX_SCORE) {
            this.score = MAX_SCORE;
        } else if(this.score + weightedScore < MIN_SCORE) {
            this.score = MIN_SCORE;
        } else {
            this.score += weightedScore;
        }

        return this.score;
    }
}
