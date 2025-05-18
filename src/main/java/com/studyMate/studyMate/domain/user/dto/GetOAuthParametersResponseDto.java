package com.studyMate.studyMate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOAuthParametersResponseDto {
    private String googleClientId;
    private String googleClientSecret;
    private String googleRedirectUrl;
    private String githubClientId;
    private String githubClientSecret;
}
