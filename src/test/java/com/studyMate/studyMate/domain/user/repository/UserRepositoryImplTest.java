package com.studyMate.studyMate.domain.user.repository;

import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.dto.GetUserRankingResponseDto;
import com.studyMate.studyMate.domain.user.dto.RankingUserDto;
import com.studyMate.studyMate.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryImplTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    User user1 = User.builder()
            .loginId("user1@test.com")
            .localLoginPw("password")
            .nickname("User1")
            .profileImg("default.jpg")
            .status(UserStatus.ACTIVE)
            .score(5000)
            .initScore(5000)
            .role(1)
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
    }

    @Test
    @DisplayName("[랭킹 조회] 1등 사용자 랭킹 조회 테스트")
    void findUsersAndRanking_withFirstPlace_shouldReturnRankOne() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result = userRepositoryImpl.findUsersAndRanking(user1.getUserId(), pageRequest);

        assertEquals(1, result.getMyRanking());
    }

    @Test
    @DisplayName("[랭킹 조회] 동점자 랭킹 순서 테스트")
    void findUsersAndRanking_withSameScore_shouldOrderByCreatedDate() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result3 = userRepositoryImpl.findUsersAndRanking(user3.getUserId(), pageRequest);
        GetUserRankingResponseDto result6 = userRepositoryImpl.findUsersAndRanking(user6.getUserId(), pageRequest);

        assertTrue(result3.getMyRanking() < result6.getMyRanking());
    }

    @Test
    @DisplayName("[랭킹 조회] 페이징 동작 테스트")
    void findUsersAndRanking_withPagination_shouldReturnCorrectPages() {
        PageRequest firstPage = PageRequest.of(0, 3);
        PageRequest secondPage = PageRequest.of(1, 3);

        GetUserRankingResponseDto firstResult = userRepositoryImpl.findUsersAndRanking(user1.getUserId(), firstPage);
        GetUserRankingResponseDto secondResult = userRepositoryImpl.findUsersAndRanking(user1.getUserId(), secondPage);

        assertEquals(3, firstResult.getOtherUsers().size());
        assertEquals(3, secondResult.getOtherUsers().size());
        assertEquals(1, firstResult.getOtherUsers().get(0).getRankNo());
        assertEquals(4, secondResult.getOtherUsers().get(0).getRankNo());
    }

    @Test
    @DisplayName("[랭킹 조회] 점수 순 정렬 테스트")
    void findUsersAndRanking_withMultipleUsers_shouldSortByScore() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result = userRepositoryImpl.findUsersAndRanking(user1.getUserId(), pageRequest);

        List<RankingUserDto> rankings = result.getOtherUsers();
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

        GetUserRankingResponseDto result = userRepositoryImpl.findUsersAndRanking(user1.getUserId(), pageRequest);

        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
    }

    @Test
    @DisplayName("[랭킹 조회] 다양한 점수 사용자 랭킹 테스트")
    void findUsersAndRanking_withVariousScores_shouldReturnCorrectRanks() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        GetUserRankingResponseDto result1 = userRepositoryImpl.findUsersAndRanking(user1.getUserId(), pageRequest);
        assertEquals(1, result1.getMyRanking());

        GetUserRankingResponseDto result2 = userRepositoryImpl.findUsersAndRanking(user2.getUserId(), pageRequest);
        assertEquals(2, result2.getMyRanking());

        GetUserRankingResponseDto result3 = userRepositoryImpl.findUsersAndRanking(user3.getUserId(), pageRequest);
        assertEquals(3, result3.getMyRanking());

        GetUserRankingResponseDto result4 = userRepositoryImpl.findUsersAndRanking(user4.getUserId(), pageRequest);
        assertEquals(6, result4.getMyRanking());

        GetUserRankingResponseDto result5 = userRepositoryImpl.findUsersAndRanking(user5.getUserId(), pageRequest);
        assertEquals(7, result5.getMyRanking());
    }
}
