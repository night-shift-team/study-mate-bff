package com.studyMate.studyMate.domain.user.dto;

import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.entity.UserOAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDto {
    private String userId;
    private String loginId;
    private String nickname;
    private String profileImg;
    private UserStatus status;
    private Integer role;
    private Integer userScore;
    private LocalDateTime registeredAt;
    private List<UserOAuthDto> userOAuth;

    public GetUserDto(User user) {
        this.userId = user.getUserId();
        this.loginId = user.getLoginId();
        this.nickname = user.getNickname();
        this.profileImg = user.getProfileImg();
        this.status = user.getStatus();
        this.role = user.getRole();
        this.userScore = user.getScore();
        this.registeredAt = user.getCreatedDt();
        this.userOAuth = user.getUserOAuths().stream()
                .map(UserOAuthDto::new)
                .collect(Collectors.toList());
    }
}
