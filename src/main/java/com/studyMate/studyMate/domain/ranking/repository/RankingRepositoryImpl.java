package com.studyMate.studyMate.domain.ranking.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.ranking.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.ranking.dto.RankingUserDto;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.redis.RedisKeyFactory;
import com.studyMate.studyMate.global.redis.RedisService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.studyMate.studyMate.domain.user.entity.QUser.user;

@Repository
@Slf4j
public class RankingRepositoryImpl implements RankingRepository {

    private final JPAQueryFactory queryFactory;
    private final RedisService redisService;

    public RankingRepositoryImpl(EntityManager entityManager, RedisService redisService) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.redisService = redisService;
    }

    private static final String RANKING_KEY = RedisKeyFactory.userRankingSortedSet();

    // 랭킹 조회 개선 (캐시미스 시, 1500ms 수준 || 캐시히트 시, 600 ms 수준)
    @Override
    public GetUserRankingResponseDto findUsersAndRankingWithRedis(String userId, Pageable pageable) {
        try {
            // Redis가 비어있는지 체크
            Long totalSize = redisService.getSortedSetSize(RANKING_KEY);

            // Initailize하고, 그래도 비었으면 EMPTY RANKING (캐시 아발란체 방지)
            if (totalSize == null || totalSize == 0) {
                log.info("[랭킹 데이터 조회] Redis 캐시 미스, 이니셜라이징");
                initializeRankings();

                // 초기화 후 재확인
                totalSize = redisService.getSortedSetSize(RANKING_KEY);
                if (totalSize == null || totalSize == 0) {
                    // "EMPTY_RANKING" 더미 키를 저장하여 캐시 아발란체 방지
                    redisService.addToSortedSet(RANKING_KEY, "EMPTY_RANKING", 0.0);
                    log.info("[랭킹 데이터 조회] 빈 랭킹 저장");
                    
                    // 바로 빈 결과 반환
                    return GetUserRankingResponseDto.builder()
                            .myRanking(null)
                            .list(new ArrayList<>())
                            .pageSize(pageable.getPageSize())
                            .pageNumber(pageable.getPageNumber())
                            .build();
                }
            }

            // 내 랭킹 조회
            Long myRankIndex = redisService.getReverseRankFromSortedSet(RANKING_KEY, userId);
            Integer myRanking = myRankIndex != null ? myRankIndex.intValue() + 1 : null;

            // 페이징 범위 계산
            long start = (long) pageable.getPageNumber() * pageable.getPageSize();
            long end = start + pageable.getPageSize() - 1;

            // 페이징 범위의 랭킹 데이터 조회
            Set<ZSetOperations.TypedTuple<String>> rangeWithScores = redisService.getRangeWithScoresFromSortedSet(RANKING_KEY, start, end);

            // userId 리스트 뽑고,
            List<String> userIds = rangeWithScores.stream()
                    .map(ZSetOperations.TypedTuple::getValue)
                    .toList();

            // IN 쿼리문으로 유저 조회
            List<User> users = queryFactory
                    .selectFrom(user)
                    .where(user.userId.in(userIds))
                    .fetch();

            Map<String, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getUserId, u -> u));

            // 랭킹 DTO 생성
            List<ZSetOperations.TypedTuple<String>> tupleList = new ArrayList<>(rangeWithScores);
            List<RankingUserDto> rankingUserDtos = new ArrayList<>();

            for (int i = 0; i < tupleList.size(); i++) {
                User foundUser = userMap.get(tupleList.get(i).getValue());
                if (foundUser == null) continue;

                // 유저 점수 계산
                int userScore = tupleList.get(i).getScore() != null 
                    ? (int) Math.floor(tupleList.get(i).getScore()) 
                    : 0;

                // DTO 추가
                rankingUserDtos.add(RankingUserDto.builder()
                    .userId(foundUser.getUserId())
                    .loginId(foundUser.getLoginId())
                    .nickname(foundUser.getNickname())
                    .profileImg(foundUser.getProfileImg())
                    .userScore(userScore)
                    .rankNo((int) start + 1 + i)
                    .build());
            }

            return GetUserRankingResponseDto.builder()
                    .myRanking(myRanking)
                    .list(rankingUserDtos)
                    .pageSize(pageable.getPageSize())
                    .pageNumber(pageable.getPageNumber())
                    .build();

        } catch (Exception e) {
            log.warn("랭킹 조회 실패 : {}", e.getMessage());
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    // 사용자 점수 업데이트 시 Redis 동기화
    @Override
    public void updateUserScoreInRedis(String userId, int newScore, long createdTimestamp) {
        double redisScore = calculateRedisScore(newScore, createdTimestamp);
        redisService.addToSortedSet(RANKING_KEY, userId, redisScore);
    }

    // Redis 점수 계산 (동점 처리용)
    // score를 정수부, user 가입일을 소수부로 결합 + 가입일 빠를수록 높은 순위
    private double calculateRedisScore(int score, long createdTimestamp) {
        double normalizedTimestamp = 1.0 - (createdTimestamp / 10000000000.0);
        return score + normalizedTimestamp;
    }

    // 전체 사용자 랭킹 초기화
    @Override
    public void initializeRankings() {
        log.info("랭킹 초기화 시작");

        List<User> allUsers = queryFactory
                .selectFrom(user)
                .fetch();

        for (User user : allUsers) {
            long createdTimestamp = user.getCreatedDt().atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
            updateUserScoreInRedis(user.getUserId(), user.getScore(), createdTimestamp);
        }

        log.info("랭킹 초기화 완료: {} 명", allUsers.size());
    }
}
