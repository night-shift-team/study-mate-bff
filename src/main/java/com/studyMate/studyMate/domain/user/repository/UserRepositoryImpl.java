package com.studyMate.studyMate.domain.user.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studyMate.studyMate.domain.user.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.user.dto.RankingUserDto;
import com.studyMate.studyMate.domain.user.entity.QUser;
import com.studyMate.studyMate.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static com.studyMate.studyMate.domain.user.entity.QUser.user;


@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public GetUserRankingResponseDto findUsersAndRanking(String userId, Pageable pageable) {
        // 나의 랭킹점수 조회
        Long gtThanMyScoreUsers = queryFactory
            .select(user.count())
            .from(user)
            .where(
                    // 나보다 점수가 높은 유저 조회;
                    user.score.gt(
                            JPAExpressions.select(user.score)
                                    .from(user)
                                    .where(user.userId.eq(userId))
                    )
                    .or(
                    // 동점자는 가입일이 빠른순으로 정렬.
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

        // 페이징에 맞는 유저 목록 조회
        List<User> userList = queryFactory
                .selectFrom(user)
                .orderBy(
                        user.score.desc(),
                        user.createdDt.asc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 3. 순위 계산
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
                            .rankNo(stRank + i) // 현재 페이지의 시작 순위 + 인덱스
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
}
