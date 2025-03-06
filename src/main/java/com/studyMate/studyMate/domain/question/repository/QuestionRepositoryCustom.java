package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.question.entity.UAQ;

import java.util.List;

public interface QuestionRepositoryCustom {
    List<MAQ> findMaqQuestions();
    List<UAQ> findUaqQuestions();
}