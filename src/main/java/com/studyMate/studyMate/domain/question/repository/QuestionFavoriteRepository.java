package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.Question;
import com.studyMate.studyMate.domain.question.entity.QuestionFavorite;
import com.studyMate.studyMate.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface QuestionFavoriteRepository extends JpaRepository<QuestionFavorite, Long> {
    Optional<QuestionFavorite> findByQuestionAndUser(Question question, User user);
}
