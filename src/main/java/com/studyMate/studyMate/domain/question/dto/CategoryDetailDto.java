package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDetailDto {
    private QuestionCategory categoryOriginName;
    private String categoryViewName;
    private Integer categoryQuestionCount;
    private Integer userSolvingCount;
    private Integer solvingLimit;
}