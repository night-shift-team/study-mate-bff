package com.studyMate.studyMate.domain.notice.dto;

import com.studyMate.studyMate.domain.notice.data.NoticeCategory;
import com.studyMate.studyMate.domain.notice.entity.Notice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class NoticeDto {
    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private NoticeCategory noticeCategory;
    private String noticePurpose;
    private String pulbisherName;

    private LocalDateTime displayStartTime;
    private LocalDateTime displayEndTime;
    private LocalDateTime maintenanceStartTime;
    private LocalDateTime maintenanceEndTime;

    public NoticeDto(Notice notice) {
        this.noticeId = notice.getId();
        this.noticeTitle = notice.getTitle();
        this.noticeContent = notice.getContent();
        this.noticeCategory = notice.getNoticeType();
        this.noticePurpose = notice.getPurpose();
        this.pulbisherName = notice.getPublisher().getNickname();
        this.displayStartTime = notice.getDisplayStartTime();
        this.displayEndTime = notice.getDisplayEndTime();
        this.maintenanceStartTime = notice.getMaintenanceStartTime();
        this.maintenanceEndTime = notice.getMaintenanceEndTime();
    }
}
