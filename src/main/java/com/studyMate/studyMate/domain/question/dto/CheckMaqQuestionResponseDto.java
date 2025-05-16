package com.studyMate.studyMate.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckMaqQuestionResponseDto {
    private String answer;
    private String answerExplanation;
    private String userAnswer;
    private Boolean isCorrect;
    private Integer reflectedScore;
    private Integer userScore;
}
