package com.studyMate.studyMate.domain.history.repository;

import com.studyMate.studyMate.domain.history.dto.SolveStatsResponseDto;


public interface QuestionHistoryRepositoryCustom {
    SolveStatsResponseDto getQuestionSolveStatsInYear(String userId);
}
