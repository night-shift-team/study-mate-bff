package com.studyMate.studyMate.domain.boards.controller;

import com.studyMate.studyMate.domain.boards.dto.SaveCommentRequestDto;
import com.studyMate.studyMate.domain.boards.service.CommentService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "게시판 API")
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("")
    @Operation(summary = "댓글 생성")
    @RoleAuth
    public Long saveComment(
            HttpServletRequest req,
            @RequestBody SaveCommentRequestDto requestDto
    ){
        String userId = (String) req.getAttribute("userId");
        return commentService.saveComment(
                requestDto.getBoardId(),
                requestDto.getContent(),
                userId
        );
    }
}
