package com.studyMate.studyMate.domain.boards.dto;

import com.studyMate.studyMate.domain.boards.data.BoardCategory;
import com.studyMate.studyMate.domain.boards.data.BoardStatus;
import com.studyMate.studyMate.domain.boards.entity.Boards;
import com.studyMate.studyMate.domain.user.dto.GetUserDto;
import com.studyMate.studyMate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private GetUserDto user;
    private String title;
    private String content;
    private BoardCategory category;
    private BoardStatus status;
    private Integer view;
    private List<CommentDto> comments;
    private LocalDateTime createdDt;

    public BoardDto(Boards board) {
        this.id = board.getId();
        this.user = new GetUserDto(board.getUser());
        this.title = board.getTitle();
        this.content = board.getContent();
        this.category = board.getCategory();
        this.status = board.getStatus();
        this.view = board.getView();
        this.createdDt = board.getCreatedDt();
        this.comments = board.getComments().stream().map(CommentDto::new).toList();
    }
}
