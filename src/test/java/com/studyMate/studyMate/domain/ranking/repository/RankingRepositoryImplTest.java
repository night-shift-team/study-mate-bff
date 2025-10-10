package com.studyMate.studyMate.domain.ranking.repository;

import com.studyMate.studyMate.RedisTestContainerConfig;
import com.studyMate.studyMate.domain.ranking.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.ranking.dto.RankingUserDto;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(RedisTestContainerConfig.class)
@Transactional
class RankingRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private RankingRepositoryImpl rankingRepository;

    User user1 = User.builder()
            .loginId("user1@test.com")
            .localLoginPw("password")
            .nickname("User1")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(5000)
            .initScore(5000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 10, 10, 0))
            .build();

    User user2 = User.builder()
            .loginId("user2@test.com")
            .localLoginPw("password")
            .nickname("User2")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(4000)
            .initScore(4000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 10, 20, 0))
            .build();

    User user3 = User.builder()
            .loginId("user3@test.com")
            .localLoginPw("password")
            .nickname("User3")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(3000)
            .initScore(3000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 10, 30, 0))
            .build();

    User user4 = User.builder()
            .loginId("user4@test.com")
            .localLoginPw("password")
            .nickname("User4")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(2000)
            .initScore(2000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 10, 40, 0))
            .build();

    User user5 = User.builder()
            .loginId("user5@test.com")
            .localLoginPw("password")
            .nickname("User5")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(1000)
            .initScore(1000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 10, 50, 0))
            .build();

    User user6 = User.builder()
            .loginId("user6@test.com")
            .localLoginPw("password")
            .nickname("User6")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(3000)
            .initScore(3000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 11, 0, 0))
            .build();

    User user7 = User.builder()
            .loginId("user7@test.com")
            .localLoginPw("password")
            .nickname("User7")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(3000)
            .initScore(3000)
            .role(1)
            .createdDt(LocalDateTime.of(2025, 10, 10, 11, 10, 0))
            .build();

    @BeforeEach
    void setUp() {
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);
        em.persist(user6);
        em.persist(user7);

        rankingRepository.initializeRankings();
    }

    @Test
    @DisplayName("[랭킹 조회] 1등 사용자 랭킹 조회 테스트")
    void findUsersAndRanking_withFirstPlace_shouldReturnRankOne() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result = rankingRepository.findUsersAndRankingWithRedis(user1.getUserId(), pageRequest);
        System.out.println(result);
        assertEquals(1, result.getMyRanking());
    }

    @Test
    @DisplayName("[랭킹 조회] 동점자 랭킹 순서 테스트")
    void findUsersAndRanking_withSameScore_shouldOrderByCreatedDate() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result3 = rankingRepository.findUsersAndRankingWithRedis(user3.getUserId(), pageRequest);
        GetUserRankingResponseDto result6 = rankingRepository.findUsersAndRankingWithRedis(user6.getUserId(), pageRequest);
        System.out.println("3번 유저 : " + result3.getMyRanking() + " || " + result6.getMyRanking());
        assertTrue(result3.getMyRanking() < result6.getMyRanking());
    }

    @Test
    @DisplayName("[랭킹 조회] 점수 순 정렬 테스트")
    void findUsersAndRanking_withMultipleUsers_shouldSortByScore() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result = rankingRepository.findUsersAndRankingWithRedis(user1.getUserId(), pageRequest);

        List<RankingUserDto> rankings = result.getList();
        for (int i = 0; i < rankings.size() - 1; i++) {
            assertTrue(rankings.get(i).getUserScore() >= rankings.get(i + 1).getUserScore());
        }
    }

    @Test
    @DisplayName("[랭킹 조회] 페이지 정보 반환 테스트")
    void findUsersAndRanking_withPageRequest_shouldReturnPageInfo() {
        int page = 1;
        int size = 5;
        PageRequest pageRequest = PageRequest.of(page, size);

        GetUserRankingResponseDto result = rankingRepository.findUsersAndRankingWithRedis(user1.getUserId(), pageRequest);

        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
    }
}