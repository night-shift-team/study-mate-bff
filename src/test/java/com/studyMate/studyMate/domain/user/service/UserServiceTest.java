package com.studyMate.studyMate.domain.user.service;

import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.dto.SignInRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignInResponseDto;
import com.studyMate.studyMate.domain.user.dto.SignUpRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignUpResponseDto;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.util.EncryptionUtil;
import com.studyMate.studyMate.global.util.JwtTokenUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EncryptionUtil encryptionUtil;

    @Autowired
    private UserService userService;

    User user1 = User.builder()
            .loginType(LoginType.LOCAL)
            .loginId("user1@studymate.com")
            .loginPw("123456")
            .nickname("user1")
            .profileImg("default.png")
            .status(UserStatus.ACTIVE)
            .score(0)
            .initScore(0)
            .role(1)
            .build();

    User user2 = User.builder()
            .loginType(LoginType.LOCAL)
            .loginId("user2@studymate.com")
            .loginPw("123456")
            .nickname("user2")
            .profileImg("default.png")
            .status(UserStatus.ACTIVE)
            .score(2000)
            .initScore(0)
            .role(2)
            .build();

    User user3 = User.builder()
            .loginType(LoginType.LOCAL)
            .loginId("user3@studymate.com")
            .loginPw("123456")
            .nickname("user3")
            .profileImg("default.png")
            .status(UserStatus.ACTIVE)
            .score(3000)
            .initScore(0)
            .role(3)
            .build();

    User userAdmin = User.builder()
            .loginType(LoginType.LOCAL)
            .loginId("admin@studymate.com")
            .loginPw("123456")
            .nickname("admin")
            .profileImg("default.png")
            .status(UserStatus.ACTIVE)
            .score(9999)
            .initScore(9999)
            .role(7)
            .build();

    @BeforeEach
    void setUp() {
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(userAdmin);
    }

    @Test
    @DisplayName("[로컬 회원가입] 성공 테스트")
    void signUpLocal_withValidRequest_shouldCreateUser() {
        SignUpRequestDto user1 = new SignUpRequestDto("userTest@studymate.com", "123456", "userTest");
        SignUpResponseDto resp = userService.signUpLocal(user1);

        assertNotNull(resp);
        assertEquals(resp.userId(), user1.loginId());
    }

    @Test
    @DisplayName("[로컬 회원가입] 중복 닉네임 테스트")
    void signUpLocal_withDuplicateNickname_shouldThrowError() {
        SignUpRequestDto request = new SignUpRequestDto(user1.getLoginId()+"1", "123456", user1.getNickname());
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.signUpLocal(request);
        });

        assertEquals(ErrorCode.DUP_USER_NICKNAME.getMessage(),exception.getMessage());
    }

    @Test
    @DisplayName("[로컬 회원가입] 중복 로그인 아이디 테스트")
    void signUpLocal_withDuplicateLoginId_shouldThrowError() {
        SignUpRequestDto request = new SignUpRequestDto(user1.getLoginId(), "123456", user1.getNickname()+"1");
        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.signUpLocal(request);
        });

        assertEquals(ErrorCode.DUP_USER_ID.getMessage(),exception.getMessage());
    }

    @Test
    @DisplayName("[로컬 로그인] 성공 테스트")
    void signInLocal_withValidRequest_shouldSignInUser() {
        String LOGIN_ID = "userTest@studymate.com";
        String LOGIN_PW = "123456";
        SignUpResponseDto resp = userService.signUpLocal(new SignUpRequestDto(LOGIN_ID, LOGIN_PW, "TEST"));

        SignInRequestDto requestDto = new SignInRequestDto(LOGIN_ID, LOGIN_PW);
        SignInResponseDto responseDto = userService.signInLocal(requestDto);

        assertNotNull(responseDto.accessToken());
        assertNotNull(responseDto.refreshToken());
    }
}