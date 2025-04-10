package com.studyMate.studyMate.domain.history.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserQuestionHistorySolveCountDto {
    private QuestionCategory questionCategory;
    private Long solveCount;
}
