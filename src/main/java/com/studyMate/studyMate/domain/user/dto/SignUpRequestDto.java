package com.studyMate.studyMate.domain.user.dto;
import jakarta.validation.constraints.*;

public record SignUpRequestDto(
        @Email(message = "invalid email")
        String loginId,
        String loginPw,
        String nickname
) { }
