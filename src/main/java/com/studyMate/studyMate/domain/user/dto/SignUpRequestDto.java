package com.studyMate.studyMate.domain.user.dto;
import jakarta.validation.constraints.*;

public record SignUpRequestDto(
        @Email(message = "login id is email")
        String loginId,
        String loginPw,
        String nickname
) { }
