package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.entity.MAQ;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class MaqQuestionPageDto {
    List<MaqQuestionDto> content;
    Integer pageSize;
    Integer pageNumber;
    Integer totalPages;

    public MaqQuestionPageDto(Page<MAQ> content) {
        this.content = content.stream().map(MaqQuestionDto::new).toList();
        this.pageSize = content.getSize();
        this.pageNumber = content.getNumber();
        this.totalPages = content.getTotalPages();
    }
}
