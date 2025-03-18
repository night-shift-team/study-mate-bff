package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionMaqRepository extends JpaRepository<MAQ, String> {
    List<MAQ> findMAQSByQuestionIdIn(List<String> questionIds);
    boolean existsByQuestionTitle(String questionTitle);
    Optional<MAQ> findByQuestionId(String questionId);
}
