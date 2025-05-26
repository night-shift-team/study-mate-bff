package com.studyMate.studyMate.domain.boards.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponseDto<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Boolean hasNext;

    public PagingResponseDto(Page<T> pageResp) {
        this.content = pageResp.getContent();
        this.page = pageResp.getNumber();
        this.size = pageResp.getSize();
        this.totalElements = pageResp.getTotalElements();
        this.totalPages = pageResp.getTotalPages();
        this.hasNext = pageResp.hasNext();
    }
}
