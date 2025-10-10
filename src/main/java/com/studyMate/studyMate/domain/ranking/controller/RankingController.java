package com.studyMate.studyMate.domain.ranking.controller;

import com.studyMate.studyMate.domain.ranking.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.ranking.service.RankingService;
import com.studyMate.studyMate.global.aop.MeasureLatency;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
@Tag(name = "Ranking API")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    @Operation(summary = "유저 랭킹 조회", description = "Redis Sorted Set 기반 랭킹 조회")
    @RoleAuth
    @MeasureLatency(label = "Redis 기반 랭킹", description = "Sorted Set 사용")
    public GetUserRankingResponseDto getUserRanking(
            HttpServletRequest request,
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit
    ) {
        String userId = (String) request.getAttribute("userId");
        return rankingService.getUserRanking(userId, page, limit);
    }

    @PostMapping("/initialize")
    @Operation(summary = "랭킹 Redis 초기화", description = "전체 사용자 랭킹을 Redis에 동기화 (관리자용)")
    @RoleAuth
    @MeasureLatency(label = "랭킹 초기화", description = "전체 유저 동기화")
    public String initializeRankings() {
        return rankingService.initializeRankings();
    }
}
