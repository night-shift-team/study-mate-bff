package com.studyMate.studyMate.domain.question.repository;

import com.querydsl.core.QueryResults;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.domain.question.dto.GetQuestionDetailResponseDto;
import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.SAQ;

import java.util.List;

public interface QuestionRepositoryCustom {
    List<MAQ> findMaqQuestionsLessThanDifficultyAndCount(int difficulty, int count);
    GetQuestionDetailResponseDto findQuestionDetailById(String id);
    QueryResults<MAQ> findRandMaqQuestionsByDifficultyAndCategoryAndPaging(int minDifficulty, int maxDifficulty, QuestionCategory category, String userId, int page, int size);
    QueryResults<SAQ> findRandSaqQuestionsByDifficultyAndCategoryAndPaging(int minDifficulty, int maxDifficulty, QuestionCategory category, String userId, int page, int size);
}