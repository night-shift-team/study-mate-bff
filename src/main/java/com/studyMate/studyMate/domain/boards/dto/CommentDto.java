package com.studyMate.studyMate.domain.boards.dto;

import com.studyMate.studyMate.domain.boards.entity.Comments;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String content;
    private String writer;
    private LocalDateTime createdDt;

    public CommentDto(Comments comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.writer = comment.getUser().getLoginId();
        this.createdDt = comment.getCreatedDt();
    }
}
