package com.studyMate.studyMate.domain.history.dto;

import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class QuestionHistoryDto {
    private Long historyId;
    private String questionId;
    private String questionTitle;
    private String userId;
    private String userAnswer;
    private Integer score;
    private Boolean isCorrect;
    private QuestionCategory questionType;
    private LocalDateTime createdDt;

    public QuestionHistoryDto(QuestionHistory questionHistory) {
        this(
                questionHistory.getId(),
                questionHistory.getQuestion().getQuestionId(),
                questionHistory.getQuestion().getQuestionTitle(),
                questionHistory.getUser().getUserId(),
                questionHistory.getQuestion().getAnswer(),
                questionHistory.getScore(),
                questionHistory.getIsCorrect(),
                questionHistory.getQType(),
                questionHistory.getCreatedDt()
        );
    }
}
