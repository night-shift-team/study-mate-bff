package com.studyMate.studyMate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGoogleAccessTokenRequest {
    private String code;
    private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String grant_type;

    public GetGoogleAccessTokenRequest withDefault(
            String code,
            String client_id,
            String client_secret,
            String redirect_uri
    ){
        this.code = code;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.grant_type = "authorization_code";

        return this;
    }
}
