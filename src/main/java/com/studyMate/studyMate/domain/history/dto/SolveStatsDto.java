package com.studyMate.studyMate.domain.history.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class SolveStatsDto {
    private String solveDay;
    private Long solveCount;

    public SolveStatsDto(
            String solveDay,
            Long solveCount
    ){
        this.solveDay = solveDay;
        this.solveCount = solveCount;
    }
}
