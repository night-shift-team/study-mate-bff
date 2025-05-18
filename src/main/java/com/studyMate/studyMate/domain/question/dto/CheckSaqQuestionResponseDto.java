package com.studyMate.studyMate.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckSaqQuestionResponseDto {
    private String keyword1;
    private String keyword2;
    private String keyword3;
    private String modelAnswer;
    private String answerExplanation;
    private String userAnswer;
    private Integer reflectedScore;
    private Integer userScore;
}
