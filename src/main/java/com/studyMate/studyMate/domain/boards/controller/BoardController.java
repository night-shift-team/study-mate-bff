package com.studyMate.studyMate.domain.boards.controller;

import com.studyMate.studyMate.domain.boards.dto.SaveBoardRequestDto;
import com.studyMate.studyMate.domain.boards.service.BoardService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
@Tag(name = "게시판 API")
public class BoardController {

    private final BoardService boardService;


    @PostMapping("/qna")
    @Operation(summary = "QnA 게시판 글 생성 기능 (Login Required)", description = "QnA 게시판 글 생성 기능 (게시글 ID를 리턴함)")
    @RoleAuth
    public Long createQnABoard(
            HttpServletRequest req,
            @RequestBody SaveBoardRequestDto requestDto
    ) {
        String userId = (String) req.getAttribute("userId");
        return this.boardService.createQNABoard(requestDto.getTitle(), requestDto.getContent(), userId);
    }
}
