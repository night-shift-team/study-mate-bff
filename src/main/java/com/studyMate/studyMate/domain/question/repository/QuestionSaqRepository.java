package com.studyMate.studyMate.domain.question.repository;

import com.studyMate.studyMate.domain.question.entity.SAQ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionSaqRepository extends JpaRepository<SAQ, String> {
}
