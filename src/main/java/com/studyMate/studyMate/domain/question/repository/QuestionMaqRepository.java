package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionMaqRepository extends JpaRepository<MAQ, Long> {
    List<MAQ> findMAQSByQuestionIdIn(List<Long> questionIds);
}
