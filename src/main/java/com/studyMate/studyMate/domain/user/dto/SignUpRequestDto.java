package com.studyMate.studyMate.domain.user.dto;

public record SignUpRequestDto(
        String loginId,
        String loginPw,
        String nickname
) { }
