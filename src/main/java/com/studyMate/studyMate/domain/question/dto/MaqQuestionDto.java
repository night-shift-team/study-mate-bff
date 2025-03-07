package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import lombok.Getter;

public record MaqQuestionDto(
    Long id,
    String description,
    String comment,
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
                maq.getDescription(),
                maq.getComment(),
                maq.getDifficulty(),
                maq.getCategory(),
                maq.getChoice1(),
                maq.getChoice2(),
                maq.getChoice3(),
                maq.getChoice4()
        );
    }
}
