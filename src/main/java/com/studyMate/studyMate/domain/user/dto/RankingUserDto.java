package com.studyMate.studyMate.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RankingUserDto {
    private String userId;
    private String loginId;
    private String nickname;
    private String profileImg;
    private Integer userScore;
    private Integer rankNo;
}
