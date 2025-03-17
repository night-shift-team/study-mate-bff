package com.studyMate.studyMate.domain.history.dto;

import com.studyMate.studyMate.domain.history.entity.QuestionHistory;

import java.util.List;

public record QuestionHistoryPageDto (
        List<QuestionHistoryDto> content,
        Integer pageSize,
        Integer pageNumber,
        Integer totalPages,
        Integer totalElements
){

    public static QuestionHistoryPageDto from(
            List<QuestionHistory> histories,
            Integer pageSize,
            Integer pageNumber,
            Integer totalPages,
            Integer totalElements
    ) {
        List<QuestionHistoryDto> dtoContent = histories.stream()
                .map(QuestionHistoryDto::new)
                .toList();

        return new QuestionHistoryPageDto(
                dtoContent,
                pageSize,
                pageNumber,
                totalPages,
                totalElements
        );
    }
}
