package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.NoArgsConstructor;

public record GetQuestionDetailResponseDto(
        Long questionId,
        String description,
        String comment,
        Integer difficulty,
        String options,
        QuestionCategory category,
        String answer
) {

    public GetQuestionDetailResponseDto(
            Long questionId,
            String description,
            String comment,
            Integer difficulty,
            String options,
            QuestionCategory category,
            String answer
    ) {
        this.questionId = questionId;
        this.description = description;
        this.comment = comment;
        this.difficulty = difficulty;
        this.options = options;
        this.category = category;
        this.answer = answer;
    }
}