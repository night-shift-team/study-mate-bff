package com.studyMate.studyMate.domain.history.controller;

import com.studyMate.studyMate.domain.history.dto.QuestionHistoryPageDto;
import com.studyMate.studyMate.domain.history.service.QuestionHistoryService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
@Tag(name = "Question History API")
public class QuestionHistoryController {

    private final QuestionHistoryService questionHistoryService;

    @GetMapping("/{month-before}/monthly")
    @Operation(summary = "유저 문제 풀이 히스토리 내역 조회", description = "Page는 0부터 시작하며, 페이지를 의미함 // size는 가져올 로우 숫자를 의미함.")
    @RoleAuth
    public QuestionHistoryPageDto getMonthlyQuestionHistoriesByUser(
            HttpServletRequest req,
            @PathVariable("month-before") int monthBefore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String userId = (String) req.getAttribute("userId");
        return questionHistoryService.findMonthlyHistoriesByUserId(userId, monthBefore, page, size);
    }
}
