package com.studyMate.studyMate.domain.history.dto;

import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;

public record QuestionHistoryDto(
        Long historyId,
        String questionId,
        String userId,
        String userAnswer,
        Integer score,
        Boolean isCorrect,
        QuestionCategory questionType
) {

    public QuestionHistoryDto(QuestionHistory questionHistory) {
        this(
                questionHistory.getId(),
                questionHistory.getQuestion().getQuestionId(),
                questionHistory.getUser().getUserId(),
                questionHistory.getQuestion().getAnswer(),
                questionHistory.getScore(),
                questionHistory.getIsCorrect(),
                questionHistory.getQType()
        );
    }
}
