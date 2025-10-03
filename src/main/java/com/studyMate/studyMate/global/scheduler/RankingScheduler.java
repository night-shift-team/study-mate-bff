package com.studyMate.studyMate.global.scheduler;

import com.studyMate.studyMate.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingScheduler {

    private final RankingRepository rankingRepository;

    // 매일 00:00:00 KST (UTC 15:00:00) 실행
    @Scheduled(cron = "0 0 15 * * ?", zone = "UTC")
    public void syncRankingToRedis() {
        log.info("=== 랭킹 Redis 동기화 스케줄러 시작 ===");
        try {
            rankingRepository.initializeRankings();
            log.info("=== 랭킹 Redis 동기화 완료 ===");
        } catch (Exception e) {
            log.error("=== 랭킹 Redis 동기화 실패: {} ===", e.getMessage(), e);
        }
    }
}
