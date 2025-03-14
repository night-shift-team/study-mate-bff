package com.studyMate.studyMate.domain.question.dto;

import lombok.Builder;

@Builder
public record CheckSaqQuestionResponseDto(
        String keyword1,
        String keyword2,
        String keyword3,
        String modelAnswer,
        String answerExplanation,
        String userAnswer,
        Integer reflectedScore,
        Integer userScore
) {
}
