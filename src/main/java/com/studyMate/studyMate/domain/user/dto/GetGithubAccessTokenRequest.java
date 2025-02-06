package com.studyMate.studyMate.domain.user.dto;

public record GetGithubAccessTokenRequest (
        String client_id,
        String client_secret,
        String code
){
}
