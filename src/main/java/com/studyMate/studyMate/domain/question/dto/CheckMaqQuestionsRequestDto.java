package com.studyMate.studyMate.domain.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CheckMaqQuestionsRequestDto {
    private List<String> questionIds;
    private List<String> userAnswers;
}
