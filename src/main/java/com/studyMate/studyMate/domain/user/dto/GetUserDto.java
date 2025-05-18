package com.studyMate.studyMate.domain.user.dto;

import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDto {
    private String userId;
    private LoginType loginType;
    private String loginId;
    private String nickname;
    private String profileImg;
    private UserStatus status;
    private Integer role;
    private Integer userScore;
    private LocalDateTime registeredAt;
}
