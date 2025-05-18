package com.studyMate.studyMate.domain.notice.repository;

import com.studyMate.studyMate.domain.notice.entity.Notice;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAll(Pageable pageable);
    Optional<Notice> findById(Long noticeId);
    List<Notice> findAllByDisplayStartTimeBeforeAndDisplayEndTimeAfter(LocalDateTime startTime, LocalDateTime endTime);
    List<Notice> findALlByMaintenanceStartTimeBeforeAndMaintenanceEndTimeAfter(LocalDateTime startTime, LocalDateTime endTime);
}
