package com.studyMate.studyMate.domain.user.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SignInResponseDto (
        String accessToken,
        String refreshToken
){ }
