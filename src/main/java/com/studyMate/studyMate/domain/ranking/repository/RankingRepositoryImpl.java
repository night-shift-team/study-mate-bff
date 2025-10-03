package com.studyMate.studyMate.domain.ranking.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.ranking.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.ranking.dto.RankingUserDto;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.redis.RedisKeyFactory;
import com.studyMate.studyMate.global.redis.RedisService;
import jakarta.persistence.EntityManager;
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
    private final EntityManager entityManager;

    public RankingRepositoryImpl(EntityManager entityManager, RedisService redisService) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.redisService = redisService;
        this.entityManager = entityManager;
    }

    private static final String RANKING_KEY = RedisKeyFactory.userRankingSortedSet();

    // DB 기반 랭킹 조회 (구 QueryDSL)
    @Override
    public GetUserRankingResponseDto findUsersAndRanking(String userId, Pageable pageable) {
        // 나보다 점수 높은 놈들 + 동점자 중에 가입일 빠른 놈들 카운트
        Long gtThanMyScoreUsers = queryFactory
            .select(user.count())
            .from(user)
            .where(
                    user.score.gt(
                            JPAExpressions.select(user.score)
                                    .from(user)
                                    .where(user.userId.eq(userId))
                    )
                    .or(
                    user.score.eq(
                            JPAExpressions.select(user.score)
                                .from(user)
                                .where(user.userId.eq(userId))
                            )
                            .and(user.createdDt.lt(
                                JPAExpressions.select(user.createdDt)
                                    .from(user)
                                    .where(user.userId.eq(userId))
                            ))
                    )
                )
                .fetchOne();

        Integer myRanking = gtThanMyScoreUsers.intValue() + 1;

        // 페이징 처리해서 유저 목록 가져오기
        List<User> userList = queryFactory
                .selectFrom(user)
                .orderBy(
                        user.score.desc(),
                        user.createdDt.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 순위 계산
        int stRank = pageable.getPageNumber() * pageable.getPageSize() + 1;

        List<RankingUserDto> rankingUserDtos = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
            rankingUserDtos.add(
                    RankingUserDto.builder()
                            .userId(u.getUserId())
                            .loginId(u.getLoginId())
                            .nickname(u.getNickname())
                            .profileImg(u.getProfileImg())
                            .userScore(u.getScore())
                            .rankNo(stRank + i)
                            .build()
            );
        }

        return GetUserRankingResponseDto.builder()
                .myRanking(myRanking)
                .otherUsers(rankingUserDtos)
                .pageSize(pageable.getPageSize())
                .pageNumber(pageable.getPageNumber())
                .build();
    }

    // Redis Sorted Set 기반 랭킹 조회 (신 Redis Sorted Set)
    @Override
    public GetUserRankingResponseDto findUsersAndRankingWithRedis(String userId, Pageable pageable) {
        try {
            // Redis가 비어있는지 체크
            Long totalSize = redisService.getSortedSetSize(RANKING_KEY);
            if (totalSize == null || totalSize == 0) {
                log.warn("Redis 랭킹 데이터가 비어있음. 초기화 시작...");
                initializeRankings();
            }

            // 내 랭킹 조회
            Long myRankIndex = redisService.getReverseRankFromSortedSet(RANKING_KEY, userId);
            Integer myRanking = myRankIndex != null ? myRankIndex.intValue() + 1 : null;

            // 페이징 범위 계산
            long start = (long) pageable.getPageNumber() * pageable.getPageSize();
            long end = start + pageable.getPageSize() - 1;

            // 해당 범위의 랭킹 데이터 조회
            Set<ZSetOperations.TypedTuple<String>> rangeWithScores =
                    redisService.getRangeWithScoresFromSortedSet(RANKING_KEY, start, end);

            // userId 리스트 추출
            List<String> userIds = rangeWithScores.stream()
                    .map(ZSetOperations.TypedTuple::getValue)
                    .collect(Collectors.toList());

            // DB에서 사용자 정보 한방에 조회
            List<User> users = entityManager.createQuery(
                    "SELECT u FROM User u WHERE u.userId IN :userIds", User.class)
                    .setParameter("userIds", userIds)
                    .getResultList();
            Map<String, User> userMap = users.stream()
                    .collect(Collectors.toMap(User::getUserId, u -> u));

            // 랭킹 DTO 생성
            List<RankingUserDto> rankingUserDtos = new ArrayList<>();
            int rank = (int) start + 1;

            for (ZSetOperations.TypedTuple<String> tuple : rangeWithScores) {
                String uid = tuple.getValue();
                User foundUser = userMap.get(uid);

                if (foundUser != null) {
                    Double redisScore = tuple.getScore();
                    int userScore = redisScore != null ? (int) Math.floor(redisScore) : 0;

                    rankingUserDtos.add(RankingUserDto.builder()
                            .userId(foundUser.getUserId())
                            .loginId(foundUser.getLoginId())
                            .nickname(foundUser.getNickname())
                            .profileImg(foundUser.getProfileImg())
                            .userScore(userScore)
                            .rankNo(rank)
                            .build());

                    rank++;
                }
            }

            return GetUserRankingResponseDto.builder()
                    .myRanking(myRanking)
                    .otherUsers(rankingUserDtos)
                    .pageSize(pageable.getPageSize())
                    .pageNumber(pageable.getPageNumber())
                    .build();

        } catch (Exception e) {
            log.warn("Redis 랭킹 조회 실패, DB로 fallback: {}", e.getMessage());
            return findUsersAndRanking(userId, pageable);
        }
    }

    // 사용자 점수 업데이트 시 Redis 동기화
    @Override
    public void updateUserScoreInRedis(String userId, int newScore, long createdTimestamp) {
        double redisScore = calculateRedisScore(newScore, createdTimestamp);
        redisService.addToSortedSet(RANKING_KEY, userId, redisScore);
    }

    // Redis 점수 계산 (동점자 처리)
    // score를 정수부, createdTimestamp를 소수부로 결합 + 가입일 빠를수록 높은 순위
    private double calculateRedisScore(int score, long createdTimestamp) {
        double normalizedTimestamp = 1.0 - (createdTimestamp / 10000000000.0);
        return score + normalizedTimestamp;
    }

    // 전체 사용자 랭킹 초기화
    @Override
    public void initializeRankings() {
        log.info("랭킹 초기화 시작");

        List<User> allUsers = entityManager.createQuery("SELECT u FROM User u", User.class)
                .getResultList();

        for (User user : allUsers) {
            long createdTimestamp = user.getCreatedDt().atZone(java.time.ZoneId.systemDefault()).toEpochSecond();
            updateUserScoreInRedis(user.getUserId(), user.getScore(), createdTimestamp);
        }

        log.info("랭킹 초기화 완료: {} 명", allUsers.size());
    }
}
