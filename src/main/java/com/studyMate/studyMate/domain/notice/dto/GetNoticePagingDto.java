package com.studyMate.studyMate.domain.notice.dto;

import com.studyMate.studyMate.domain.notice.entity.Notice;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class GetNoticePagingDto {
    List<NoticeDto> content;
    Integer pageSize;
    Integer pageNumber;
    Integer totalPages;

    public GetNoticePagingDto(Page<Notice> content) {
        this.content = content.stream().map(NoticeDto::new).toList();
        this.pageSize = content.getTotalPages();
        this.pageNumber = content.getNumber();
        this.totalPages = content.getTotalPages();
    }
}
