package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.question.entity.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {
    List<QuestionHistory>findQuestionHistoriesByUser_UserIdAndQuestion_QuestionId(Long userId, Long questionId);
}
