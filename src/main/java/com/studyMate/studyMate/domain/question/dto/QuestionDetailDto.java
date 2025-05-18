package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.entity.Question;
import lombok.Getter;

@Getter
public class QuestionDetailDto {

    private final String questionId;
    private final String questionTitle;
    private final String questionContent;
    private final QuestionCategory questionCategory;
    private final String questionAnswer;
    private final String questionExplanation;
    private final Integer difficulty;

    public QuestionDetailDto(Question question){
        this.questionId = question.getQuestionId();
        this.questionTitle = question.getQuestionTitle();
        this.questionContent = question.getContent();
        this.questionCategory = question.getCategory();
        this.questionAnswer = question.getAnswer();
        this.questionExplanation = question.getAnswerExplanation();
        this.difficulty = question.getDifficulty();
    }
}
