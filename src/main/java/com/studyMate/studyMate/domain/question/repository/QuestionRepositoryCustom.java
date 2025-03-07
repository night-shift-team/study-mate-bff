package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;

import java.util.List;

public interface QuestionRepositoryCustom {
    List<MAQ> findMaqQuestionsLessThanDifficultyAndCount(int difficulty, int count);
    List<MAQ> findMaqQuestions();
}