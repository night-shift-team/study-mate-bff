package com.studyMate.studyMate.domain.user.dto;

public record GetGoogleAccessTokenRequest(
        String code,
        String client_id,
        String client_secret,
        String redirect_uri,
        String grant_type
) {
    public static GetGoogleAccessTokenRequest withDefault(String code, String client_id, String client_secret, String redirect_uri){
        return new GetGoogleAccessTokenRequest(code, client_id, client_secret, redirect_uri, "authorization_code");
    }
}
