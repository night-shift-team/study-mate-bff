package com.studyMate.studyMate.domain.question.repository;

import com.querydsl.core.QueryResults;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.dto.GetQuestionDetailResponseDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.SAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionRepositoryCustom {
    List<MAQ> findMaqQuestionsLessThanDifficultyAndCount(int difficulty, int count);
    GetQuestionDetailResponseDto findQuestionDetailById(String id);
    Page<MAQ> findRandMaqQuestionsByDifficultyAndCategoryAndPaging(int minDifficulty, int maxDifficulty, QuestionCategory category, String userId, Pageable pageable);
    Page<SAQ> findRandSaqQuestionsByDifficultyAndCategoryAndPaging(int minDifficulty, int maxDifficulty, QuestionCategory category, String userId, Pageable pageable);
    Page<MAQ> findMaqQuestionsByKeyword(String keyword, Pageable pageable);
}