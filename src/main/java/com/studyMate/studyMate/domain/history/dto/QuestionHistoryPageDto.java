package com.studyMate.studyMate.domain.history.dto;

import com.studyMate.studyMate.domain.history.entity.QuestionHistory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class QuestionHistoryPageDto {
    List<QuestionHistoryDto> content;
    Integer pageSize;
    Integer pageNumber;
    Integer totalPages;
    Integer totalElements;

    public QuestionHistoryPageDto(
            List<QuestionHistory> histories,
            Integer pageSize,
            Integer pageNumber,
            Integer totalPages,
            Integer totalElements
    ) {
        this.content = histories.stream()
                .map(QuestionHistoryDto::new)
                .toList();
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
