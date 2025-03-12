package com.studyMate.studyMate.domain.history.service;

import com.studyMate.studyMate.domain.history.dto.QuestionHistoryDto;
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

    /**
     * 유저가 Question ID의 문제를 풀었던 내역들을 모두 가져온다. (문제를 중복으로 여러번 풀 수도 있기 때문에)
     * @param userId 유저의 아이디 값
     * @param questionId 문제의 아이디 값
     * @return List<QuestionHistoryDto>
     */
    public List<QuestionHistoryDto> findHistoriesByQuestionIdAndUserId(String userId, String questionId) {
        return questionHistoryRepository
                .findQuestionHistoriesByUser_UserIdAndQuestion_QuestionId(userId, questionId)
                .stream()
                .map(QuestionHistoryDto::new)
                .toList();
    }

    /**
     * Question History 목록을 저장하는 메소드
     * @param questionHistories 문제 내역
     * @return List<QuestionHistoryDto>
     */
    public List<QuestionHistoryDto> saveQuestionHistories(List<QuestionHistory> questionHistories) {
        return questionHistoryRepository.saveAll(questionHistories)
                .stream()
                .map(QuestionHistoryDto::new)
                .toList();
    }
}
