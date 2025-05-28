package com.studyMate.studyMate.domain.boards.repository;

import com.studyMate.studyMate.domain.boards.data.BoardCategory;
import com.studyMate.studyMate.domain.boards.entity.Boards;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {
    Page<Boards> findAllByCategory(BoardCategory category, Pageable pageable);
    Optional<Boards> findByIdAndCategory(Long id, BoardCategory category);
}
