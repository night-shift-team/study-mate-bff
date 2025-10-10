package com.studyMate.studyMate.domain.ranking.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Builder
@Getter
public class GetUserRankingResponseDto {
    private Integer myRanking;
    private List<RankingUserDto> list;

    private Integer pageSize;
    private Integer pageNumber;

    public GetUserRankingResponseDto(
            Integer myRanking,
            List<RankingUserDto> list,
            Integer pageSize,
            Integer pageNumber
    ) {
        this.myRanking = myRanking;
        this.list = list;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
