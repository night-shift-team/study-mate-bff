package com.studyMate.studyMate.domain.history.service;

import com.studyMate.studyMate.domain.history.dto.QuestionHistoryDto;
import com.studyMate.studyMate.domain.history.dto.QuestionHistoryPageDto;
import com.studyMate.studyMate.domain.history.dto.SolveStatsResponseDto;
import com.studyMate.studyMate.domain.history.repository.QuestionHistoryRepository;
import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class QuestionHistoryService {

    private final QuestionHistoryRepository questionHistoryRepository;

    /**
     * 유저의 잔디를 뿌리기위해 날짜별로, 1년간 일자별로 몇개의 문제를 풀었는지 내역
     */
    public SolveStatsResponseDto getSolveStatsByUserId(String userId){
        return questionHistoryRepository.getQuestionSolveStatsInYear(userId);
    }

    /**
     * 유저의 문제풀이 히스토리 내역을 1년 내 기록을 페이징으로 가져온다.
     * @param userId 유저의 아이디 값
     * @param page 원하는 페이지 숫자 (0 ~)
     * @param size 원하는 데이터 숫자
     * @return List<QuestionHistoryDto>
     */
    public QuestionHistoryPageDto findMonthlyHistoriesByUserId(String userId, int monthBefore, int page, int size) {
        LocalDateTime oneYearAgo = LocalDateTime.now().minusMonths(monthBefore);
        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdDt").descending());
        Page<QuestionHistory> content = questionHistoryRepository.findQuestionHistoriesByUser_UserIdAndCreatedDtAfter(userId, oneYearAgo, pageable);

        return QuestionHistoryPageDto.from(
                content.getContent(),
                content.getTotalPages(),
                content.getNumber(),
                content.getTotalPages(),
                (int) content.getTotalElements()
        );
    }

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
