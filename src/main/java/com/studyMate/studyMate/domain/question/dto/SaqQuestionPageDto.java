package com.studyMate.studyMate.domain.question.dto;

import com.studyMate.studyMate.domain.question.entity.SAQ;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class SaqQuestionPageDto {
    List<SaqQuestionDto> content;
    Integer pageSize;
    Integer pageNumber;
    Integer totalPages;

    public SaqQuestionPageDto(
        Page<SAQ> content
    ){
        this.content = content.getContent().stream().map(SaqQuestionDto::new).toList();
        this.pageSize = content.getTotalPages();
        this.pageNumber = content.getNumber();
        this.totalPages = content.getTotalPages();
    }
}
