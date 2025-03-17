package com.studyMate.studyMate.domain.history.repository;

import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
    Page<QuestionHistory> findQuestionHistoriesByUser_UserIdAndCreatedDtAfter(String userId, LocalDateTime createdDtAfter, Pageable pageable);
    List<QuestionHistory>findQuestionHistoriesByUser_UserIdAndQuestion_QuestionId(String userId, String questionId);
}
