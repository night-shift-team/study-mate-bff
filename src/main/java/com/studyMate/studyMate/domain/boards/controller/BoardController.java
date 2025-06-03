package com.studyMate.studyMate.domain.boards.controller;

import com.studyMate.studyMate.domain.boards.dto.BoardDto;
import com.studyMate.studyMate.domain.boards.dto.PagingResponseDto;
import com.studyMate.studyMate.domain.boards.dto.SaveBoardRequestDto;
import com.studyMate.studyMate.domain.boards.service.BoardService;
import com.studyMate.studyMate.global.config.RoleAuth;
import com.sun.jdi.LongValue;
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

    @GetMapping("/{id}/qna")
    @Operation(summary = "QnA 게시글 조회", description = "QnA 게시글 id로 조회")
    @RoleAuth
    public BoardDto getBoardById(
            HttpServletRequest req,
            @PathVariable("id") Long id
    ) {
        String userId = (String) req.getAttribute("userId");
        return this.boardService.getQnaBoardById(id, userId);
    }

    @GetMapping("qna")
    @Operation(summary = "QnA 게시글 목록 조회", description = "QnA 게시글 목록을 조회")
    public PagingResponseDto<BoardDto> getQnaBoard(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        return this.boardService.getQNABoardsPaging(page, limit);
    }

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

    @DeleteMapping("/{id}")
    @RoleAuth
    public boolean deleteByBoardId(
            HttpServletRequest req,
            @PathVariable("id") Long id
    ) {
        String userId = (String) req.getAttribute("userId");
        return this.boardService.deleteBoard(id, userId);
    }
}
