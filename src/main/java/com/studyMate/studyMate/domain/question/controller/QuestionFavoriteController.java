package com.studyMate.studyMate.domain.question.controller;

import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionRequestDto;
import com.studyMate.studyMate.domain.question.dto.CheckMaqQuestionResponseDto;
import com.studyMate.studyMate.domain.question.service.QuestionFavoriteService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question-favorite")
@Tag(name = "Question Favorite API")
public class QuestionFavoriteController {

    private final QuestionFavoriteService questionFavoriteService;

    @PostMapping("/{questionId}")
    @Operation(summary = "문제 즐겨찾기 추가/제거 (Toggle) API", description = "문제 즐겨찾기 추가/제거 (Toggle) API")
    @RoleAuth
    public boolean toggleQuestionFavorite(
            HttpServletRequest req,
            @PathVariable("questionId") String questionId
    ) {
        String userId = (String) req.getAttribute("userId");
        return questionFavoriteService.toggleQuestionFavorite(questionId, userId);
    }
}
