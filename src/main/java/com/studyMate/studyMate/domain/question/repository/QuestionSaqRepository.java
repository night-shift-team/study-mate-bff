package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.SAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionSaqRepository extends JpaRepository<SAQ, String> {
    boolean existsByQuestionTitle(String questionTitle);
    Optional<SAQ> findByQuestionId(String questionId);
}
