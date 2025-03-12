package com.studyMate.studyMate.domain.history.service;

import com.studyMate.studyMate.domain.history.repository.QuestionHistoryRepository;
import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class QuestionHistoryService {

    private final QuestionHistoryRepository questionHistoryRepository;

    public List<QuestionHistory> findHistoriesByQuestionIdAndUserId(String userId, String questionId) {
        return questionHistoryRepository.findQuestionHistoriesByUser_UserIdAndQuestion_QuestionId(userId, questionId);
    }

    public List<QuestionHistory> saveQuestionHistories(List<QuestionHistory> questionHistories) {
        return questionHistoryRepository.saveAll(questionHistories);
    }
}
