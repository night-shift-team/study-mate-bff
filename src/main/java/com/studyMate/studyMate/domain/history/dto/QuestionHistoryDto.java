package com.studyMate.studyMate.domain.history.dto;

import com.studyMate.studyMate.domain.question.data.QuestionCategory;

public record QuestionHistoryDto(
        Long hisotoryId,
        Long userId,
        String userAnswer,
        Integer score,
        Boolean isCorrect,
        QuestionCategory questionType
) {
}
