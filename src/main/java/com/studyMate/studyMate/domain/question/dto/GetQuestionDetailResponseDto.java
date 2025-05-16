package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionDetailResponseDto {
    private String questionId;
    private String questionTitle;
    private String content;
    private Integer difficulty;
    private String options;
    private QuestionCategory category;
    private String answer;
    private String answerExplanation;
}
