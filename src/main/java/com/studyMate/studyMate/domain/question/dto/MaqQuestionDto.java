package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MaqQuestionDto {
    private String id;
    private String questionTitle;
    private String content;
    private Integer difficulty;
    private QuestionCategory category;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;

    public MaqQuestionDto(MAQ maq) {
        this.id = maq.getQuestionId();
        this.questionTitle = maq.getQuestionTitle();
        this.content = maq.getContent();
        this.difficulty = maq.getDifficulty();
        this.category = maq.getCategory();
        this.choice1 = maq.getChoice1();
        this.choice2 = maq.getChoice2();
        this.choice3 = maq.getChoice3();
        this.choice4 = maq.getChoice4();
    }
}
