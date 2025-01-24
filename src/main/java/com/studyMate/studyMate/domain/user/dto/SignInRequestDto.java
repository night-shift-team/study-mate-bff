package com.studyMate.studyMate.domain.user.dto;

import jakarta.validation.constraints.Email;

public record SignInRequestDto(
        @Email(message = "invalid email")
        String loginId,
        String loginPw
) {
}
