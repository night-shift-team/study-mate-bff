package com.studyMate.studyMate.domain.user.dto;

public record GetGithubAccessTokenResponse (
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in,
        String scope,
        String token_type
){
}
