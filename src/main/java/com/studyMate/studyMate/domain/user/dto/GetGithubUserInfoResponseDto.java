package com.studyMate.studyMate.domain.user.dto;

public record GetGithubUserInfoResponseDto (
        String login,
        String id,
        String avatar_url,
        String email
){
}
