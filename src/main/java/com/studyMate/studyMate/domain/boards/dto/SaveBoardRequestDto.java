package com.studyMate.studyMate.domain.boards.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SaveBoardRequestDto {
    private String title;
    private String content;
}
