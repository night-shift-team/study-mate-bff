package com.studyMate.studyMate.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetGithubUserInfoResponseDto {
    private String login;
    private String id;
    private String avatar_url;
    private String email;
}
