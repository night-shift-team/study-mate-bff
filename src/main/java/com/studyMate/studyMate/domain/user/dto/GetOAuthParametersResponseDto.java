package com.studyMate.studyMate.domain.user.dto;

import lombok.Builder;

@Builder
public record GetOAuthParametersResponseDto(
        String googleClientId,
        String googleClientSecret,
        String googleRedirectUrl,
        String githubClientId,
        String githubClientSecret
){
}
