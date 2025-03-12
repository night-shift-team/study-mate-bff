package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.NoArgsConstructor;

public record GetQuestionDetailResponseDto(
        String questionId,
        String questionTitle,
        String content,
        Integer difficulty,
        String options,
        QuestionCategory category,
        String answer,
        String answerExplanation
) {

    public GetQuestionDetailResponseDto(
            String questionId,
            String questionTitle,
            String content,
            Integer difficulty,
            String options,
            QuestionCategory category,
            String answer,
            String answerExplanation
    ) {
        this.questionId = questionId;
        this.questionTitle = questionTitle;
        this.content = content;
        this.difficulty = difficulty;
        this.options = options;
        this.category = category;
        this.answer = answer;
        this.answerExplanation = answerExplanation;
    }
}