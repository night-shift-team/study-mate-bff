package com.studyMate.studyMate.domain.boards.repository;

import com.studyMate.studyMate.domain.boards.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Boards, Long> {
}
