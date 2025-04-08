package com.studyMate.studyMate.domain.history.controller;

import com.studyMate.studyMate.domain.history.dto.QuestionHistoryDto;
import com.studyMate.studyMate.domain.history.dto.QuestionHistoryPageDto;
import com.studyMate.studyMate.domain.history.dto.SolveStatsResponseDto;
import com.studyMate.studyMate.domain.history.service.QuestionHistoryService;
import com.studyMate.studyMate.domain.question.data.QuestionCategory;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/history")
@Tag(name = "Question History API")
public class QuestionHistoryController {

    private final QuestionHistoryService questionHistoryService;

    @GetMapping("{category}/category/today")
    @Operation(summary = "유저의 금일 카테고리별 문제 풀이 내역 조회", description = "유저의 금일 문제풀이 내역을 카테고리 별로 조회하여 어떤 문제를 풀었는지 알 수 있다. (정답, 오답 모두 포함)")
    @RoleAuth
    public List<QuestionHistoryDto> getTodayQuestionHistoriesByUserAndCategory(
            HttpServletRequest req,
            @Parameter() QuestionCategory category
    ){
        String userId = (String) req.getAttribute("userId");
        return questionHistoryService.findTodayQuestionHistoriesByCategory(userId, category);
    }

    @GetMapping("/{month-before}/monthly")
    @Operation(summary = "유저 문제 풀이 히스토리 내역 조회", description = "Page는 0부터 시작하며, 페이지를 의미함 (Default = 0)// size는 가져올 로우 숫자를 의미함. (Default = 10)")
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

    // 날짜 별로 몇개 문제 풀었냐.
    @GetMapping("/solve-stats")
    @Operation(summary = "잔디밭용 날짜 별 문제풀이 기록 (1년간 기록)", description = "날짜 별 문제풀이 수 기록 확인 (1년간 기록)")
    @RoleAuth
    public SolveStatsResponseDto getRecentSolveStats(
            HttpServletRequest req
    ) {
        String userId = (String) req.getAttribute("userId");
        return questionHistoryService.getSolveStatsByUserId(userId);
    }
}
