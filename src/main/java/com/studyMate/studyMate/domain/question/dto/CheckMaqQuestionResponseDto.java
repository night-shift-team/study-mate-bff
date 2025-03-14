package com.studyMate.studyMate.domain.question.dto;

import lombok.Builder;

@Builder
public record CheckMaqQuestionResponseDto(
    String answer,
    String answerExplanation,
    String userAnswer,
    Boolean isCorrect,
    Integer reflectedScore,
    Integer userScore
) {
}
