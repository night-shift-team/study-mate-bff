package com.studyMate.studyMate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGoogleAccessTokenResponseDto {
    private String access_token;
    private Integer expires_in;
    private String scope;
    private String token_type;
    private String id_token;
}
