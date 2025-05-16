package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.entity.SAQ;
import lombok.Getter;

@Getter
public class SaqQuestionDto {
    private final String id;
    private final String questionTitle;
    private final String content;
    private final Integer difficulty;
    private final QuestionCategory category;

    public SaqQuestionDto(SAQ saq) {
        this.id = saq.getQuestionId();
        this.questionTitle = saq.getQuestionTitle();
        this.content = saq.getContent();
        this.difficulty = saq.getDifficulty();
        this.category = saq.getCategory();
    }
}
