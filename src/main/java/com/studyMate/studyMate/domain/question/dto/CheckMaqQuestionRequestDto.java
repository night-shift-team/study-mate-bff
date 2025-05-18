package com.studyMate.studyMate.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckMaqQuestionRequestDto {
    private String questionId;
    private String userAnswer;
}
