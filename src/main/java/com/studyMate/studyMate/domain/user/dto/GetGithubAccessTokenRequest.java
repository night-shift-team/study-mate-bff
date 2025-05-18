package com.studyMate.studyMate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGithubAccessTokenRequest {
    private String client_id;
    private String client_secret;
    private String code;
}
