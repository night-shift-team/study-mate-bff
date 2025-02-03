package com.studyMate.studyMate.domain.user.dto;

public record GetGoogleAccessTokenResponseDto(
        String access_token,
        Integer expires_in,
        String scope,
        String token_type,
        String id_token
) {
}
