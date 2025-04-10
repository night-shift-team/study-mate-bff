package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetQuestionCategoryInfoResponseDto {
    private Integer totalCategoryCount;
    private List<CategoryDetailDto> detail;
}
