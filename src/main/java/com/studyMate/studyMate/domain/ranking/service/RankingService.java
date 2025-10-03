package com.studyMate.studyMate.domain.ranking.service;

import com.studyMate.studyMate.domain.ranking.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.ranking.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final RankingRepository rankingRepository;

    // Redis 기반 랭킹 조회
    public GetUserRankingResponseDto getUserRanking(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return rankingRepository.findUsersAndRankingWithRedis(userId, pageRequest);
    }

    // DB 기반 랭킹 조회 (성능 비교용)
    public GetUserRankingResponseDto getUserRankingLegacy(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return rankingRepository.findUsersAndRanking(userId, pageRequest);
    }

    // Redis 랭킹 초기화
    @Transactional
    public String initializeRankings() {
        rankingRepository.initializeRankings();
        return "ok";
    }

    // 점수 업데이트 시 Redis 동기화 (다른 모듈에서 호출)
    public void updateUserScore(String userId, int newScore, long createdTimestamp) {
        rankingRepository.updateUserScoreInRedis(userId, newScore, createdTimestamp);
    }
}
