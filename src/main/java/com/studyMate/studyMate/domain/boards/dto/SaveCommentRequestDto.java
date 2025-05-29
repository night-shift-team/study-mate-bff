package com.studyMate.studyMate.domain.boards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveCommentRequestDto {
    private Long boardId;
    private String content;
}
