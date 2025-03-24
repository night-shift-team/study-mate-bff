package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

@Getter
@Builder
public class CreateMaqQuestionRequestDto {
    @NotNull(message = "invalid question title")
    private String questionTitle;
    @NotNull(message = "invalid question content")
    private String questionContent;
    @NotNull(message = "invalid question answer")
    private String answer;
    @NotNull(message = "invalid question expalanation")
    private String answerExplanation;
    @Min(value = 1, message = "difficulty should more than zero")
    @Max(value = 100, message = "difficulty less than 101")
    private Integer difficulty;
    private QuestionCategory category;
    @NotNull(message = "invalid question choice1")
    private String choice1;
    @NotNull(message = "invalid question choice2")
    private String choice2;
    @NotNull(message = "invalid question choice3")
    private String choice3;
    @NotNull(message = "invalid question choice4")
    private String choice4;
}
