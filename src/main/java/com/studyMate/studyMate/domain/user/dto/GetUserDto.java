package com.studyMate.studyMate.domain.user.dto;

import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
public record GetUserDto(
        String userId,
        LoginType loginType,
        String loginId,
        String nickname,
        String profileImg,
        UserStatus status,
        Integer role,
        Integer userScore,
        LocalDateTime registeredAt
) {
}
