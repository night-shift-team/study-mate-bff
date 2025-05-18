package com.studyMate.studyMate.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class GetUserRankingResponseDto {
    private Integer myRanking;
    private List<RankingUserDto> otherUsers;

    private Integer pageSize;
    private Integer pageNumber;

    public GetUserRankingResponseDto(
            Integer myRanking,
            List<RankingUserDto> otherUsers,
            Integer pageSize,
            Integer pageNumber
    ) {
        this.myRanking = myRanking;
        this.otherUsers = otherUsers;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
