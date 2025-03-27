package com.studyMate.studyMate.domain.notice.dto;

import com.studyMate.studyMate.domain.notice.entity.Notice;
import lombok.Getter;

import java.util.List;

@Getter
public class GetNoticeWithMaintenanceDto {
    List<NoticeDto> displayNotices;
    List<NoticeDto> maintenaceNotices;
    Boolean isMaintenanceNoticeExist;
    Boolean isDisplayNoticeExist;

    public GetNoticeWithMaintenanceDto(
            List<Notice> displayNotices,
            List<Notice> maintenaceNotices,
            boolean isDisplayNoticeExist,
            boolean isMaintenanceNow
    ) {
        this.displayNotices = displayNotices.stream().map(NoticeDto::new).toList();
        this.maintenaceNotices = maintenaceNotices.stream().map(NoticeDto::new).toList();
        this.isDisplayNoticeExist = isDisplayNoticeExist;
        this.isMaintenanceNoticeExist = isMaintenanceNow;
    }
}