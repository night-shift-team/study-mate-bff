package com.studyMate.studyMate.domain.user.dto;

import java.time.LocalDateTime;

public record SignInResponseDto (
        String accessToken,
        String refreshToken
){ }
