package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import com.studyMate.studyMate.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String>, QuestionRepositoryCustom {

    Optional<Question> findByQuestionId(String questionId);
}
