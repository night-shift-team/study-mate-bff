package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.SAQ;

public record SaqQuestionDto (
        String id,
        String questionTitle,
        String content,
        Integer difficulty,
        QuestionCategory category
) {

    public SaqQuestionDto(SAQ maq) {
        this(
                maq.getQuestionId(),
                maq.getQuestionTitle(),
                maq.getContent(),
                maq.getDifficulty(),
                maq.getCategory()
        );
    }
}
