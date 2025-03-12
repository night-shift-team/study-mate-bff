package com.studyMate.studyMate.domain.history.repository;

import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
    List<QuestionHistory>findQuestionHistoriesByUser_UserIdAndQuestion_QuestionId(String userId, String questionId);
}
