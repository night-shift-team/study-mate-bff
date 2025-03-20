package com.studyMate.studyMate.domain.history.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class SolveStatsResponseDto {
    List<SolveStatsDto> solveStats;
    LocalDate StartDate;
    LocalDate EndDate;
}
