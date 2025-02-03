package com.studyMate.studyMate.domain.user.dto;

public record GetGoogleUserInfoResponseDto(
        String id,
        String email,
        boolean verified_email,
        String name,
        String given_name,
        String family_name,
        String picture
) {
}
