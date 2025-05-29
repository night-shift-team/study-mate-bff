package com.studyMate.studyMate.domain.boards.repository;

import com.studyMate.studyMate.domain.boards.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {
}
