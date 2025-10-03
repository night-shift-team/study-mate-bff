package com.studyMate.studyMate.domain.ranking.repository;

import com.studyMate.studyMate.domain.ranking.dto.GetUserRankingResponseDto;
import org.springframework.data.domain.Pageable;

public interface RankingRepository {
    GetUserRankingResponseDto findUsersAndRanking(String userId, Pageable pageable);
    GetUserRankingResponseDto findUsersAndRankingWithRedis(String userId, Pageable pageable);
    void updateUserScoreInRedis(String userId, int newScore, long createdTimestamp);
    void initializeRankings();
}
