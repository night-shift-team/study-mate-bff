package com.studyMate.studyMate.domain.history.repository;

import com.studyMate.studyMate.domain.history.dto.QuestionHistoryDto;
import com.studyMate.studyMate.domain.history.dto.SolveStatsResponseDto;
import com.studyMate.studyMate.domain.history.dto.UserQuestionHistorySolveCountDto;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;

import java.time.LocalDateTime;
import java.util.List;


public interface QuestionHistoryRepositoryCustom {
    SolveStatsResponseDto getQuestionSolveStatsInYear(String userId);
    List<QuestionHistoryDto> getQuestionHistoryByUserIdAndQTypeAndDateAfter(String userId, QuestionCategory qType, LocalDateTime timeAfter);
    List<UserQuestionHistorySolveCountDto> getTodayUserQuestionHistorySolveCount(String userId, LocalDateTime todayStartTime);
}
