package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import lombok.Getter;

public record MaqQuestionDto(
    String id,
    String questionTitle,
    String content,
    Integer difficulty,
    QuestionCategory category,
    String choice1,
    String choice2,
    String choice3,
    String choice4
) {
    public MaqQuestionDto(MAQ maq) {
        this(
                maq.getQuestionId(),
                maq.getQuestionTitle(),
                maq.getContent(),
                maq.getDifficulty(),
                maq.getCategory(),
                maq.getChoice1(),
                maq.getChoice2(),
                maq.getChoice3(),
                maq.getChoice4()
        );
    }
}
