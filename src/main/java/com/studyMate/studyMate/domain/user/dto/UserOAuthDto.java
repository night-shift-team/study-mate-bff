package com.studyMate.studyMate.domain.user.dto;

import com.studyMate.studyMate.domain.user.data.OAuthType;
import com.studyMate.studyMate.domain.user.entity.UserOAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserOAuthDto {
    private OAuthType oauthType;
    private String accessToken;

    public UserOAuthDto(UserOAuth userOAuth) {
        this.oauthType = userOAuth.getOauthType();
        this.accessToken = userOAuth.getAccessToken();
    }
}
