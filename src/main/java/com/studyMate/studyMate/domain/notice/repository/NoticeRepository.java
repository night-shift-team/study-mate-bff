package com.studyMate.studyMate.domain.notice.repository;

import com.studyMate.studyMate.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Page<Notice> findAll(Pageable pageable);
    List<Notice> findAllByDisplayStartTimeBeforeAndDisplayEndTimeAfter(LocalDateTime startTime, LocalDateTime endTime);
    List<Notice> findALlByMaintenanceStartTimeBeforeAndMaintenanceEndTimeAfter(LocalDateTime startTime, LocalDateTime endTime);
}
