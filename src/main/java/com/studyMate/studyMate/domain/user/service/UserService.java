package com.studyMate.studyMate.domain.user.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.dto.*;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.util.EncryptionUtil;
import com.studyMate.studyMate.global.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final EncryptionUtil encryptionUtil;

    private final String GOOGLE_GET_ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_GET_USERINFO_BASE_URL = "https://www.googleapis.com/userinfo/v2/me?access_token=";

    private final String GITHUB_GET_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private final String GITHUB_GET_USERINFO_BASE_URL = "https://api.github.com/user";

    @Value("${e.auth.google_client_id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${e.auth.google_client_secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${e.auth.redirect_url}")
    private String FRONT_REDIRECT_URL;

    @Value("${e.auth.github_client_id}")
    private String GITHUB_CLIENT_ID;
    @Value("{e.auth.github_client_secret}")
    private String GITHUB_CLIENT_SECRET;

    public GetUserDto getActiveUserById(long id) {
        User user = userRepository.findByUserIdAndStatus(id, UserStatus.ACTIVE).orElseThrow(() ->  new CustomException(ErrorCode.NOT_ACTIVE_USER));

        return GetUserDto.builder()
                .userId(user.getUserId())
                .loginType(user.getLoginType())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .status(user.getStatus())
                .role(user.getRole())
                .registeredAt(user.getCreatedDt())
                .build();
    }

    /**
     * 로컬 회원가입 메소드
     * @param signUpRequestDto
     * @return signUpResponseDto
     */
    @Transactional
    public SignUpResponseDto signUpLocal(SignUpRequestDto signUpRequestDto) {
        // 1. 중복확인
        boolean isLoginIdValid = checkDuplicateEmail(signUpRequestDto.loginId());
        if(isLoginIdValid){
            throw new CustomException(ErrorCode.DUP_USER_ID);
        }

        boolean isNicknameValid = checkDuplicateNickname(signUpRequestDto.nickname());
        if(isNicknameValid){
            throw new CustomException(ErrorCode.DUP_USER_NICKNAME);
        }

        // 2. 비밀번호 암호화
        String encryptedUserPw = encryptionUtil.encryptBcrypt(signUpRequestDto.loginPw());

        // 3. 유저 생성
        User newUser = createUser(LoginType.LOCAL, signUpRequestDto.loginId(), encryptedUserPw, signUpRequestDto.nickname(), "default", 0);

        return new SignUpResponseDto(newUser.getUserId(), newUser.getLoginId());
    }

    public SignInResponseDto signInLocal(SignInRequestDto signInRequestDto) {
        // 1. Login Id로 유저 확인
        User user = userRepository.findByLoginId(signInRequestDto.loginId()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGINID));

        // 2. 비밀번호 검증 비교
        boolean isValidPw = encryptionUtil.compareBcrypt(signInRequestDto.loginPw(), user.getLoginPw());
        if(!isValidPw){
            throw new CustomException(ErrorCode.INVALID_LOGINPW);
        }

        // 3. JWT Token 발급
        String acToken = jwtTokenUtil.generateToken(user.getUserId(), JwtTokenUtil.TokenType.ACCESS);
        String rfToken = jwtTokenUtil.generateToken(user.getUserId(), JwtTokenUtil.TokenType.REFRESH);

        return new SignInResponseDto(acToken, rfToken);
    }

    /**
     * 구글 로그인 - 유저 호출 api 메소드
     */
    @Transactional
    public SignInResponseDto signInGoogle(String encodedGoogleCode) {
        try {
            // 1. Get Access Token
            String gAccessToken = getGoogleAccessToken(encodedGoogleCode);

            // 2. Get User info
            GetGoogleUserInfoResponseDto userInfo = getGoogleUserInfo(gAccessToken);

            // 3.1 DB 유저 존재확인 -> 있으면 해당 유저 사용
            User user = userRepository.findByLoginId(userInfo.email()).orElseGet(() -> {
                // 3.2 없으면, 새로운 유저 생성
                return this.createUser(
                        LoginType.GOOGLE,
                        userInfo.email(),
                        "google",
                        userInfo.name(),
                        userInfo.picture(),
                        0
                );
            });

            // 4. 토큰 페어 발급 -> 리턴
            String acToken = jwtTokenUtil.generateToken(user.getUserId(), JwtTokenUtil.TokenType.ACCESS);
            String rfToken = jwtTokenUtil.generateToken(user.getUserId(), JwtTokenUtil.TokenType.REFRESH);

            return new SignInResponseDto(acToken, rfToken);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_GOOGLE_AUTH_CODE);
        }
    }


    /**
     * Github 로그인 - 유저 호출 api 메소드
     * @return SignInResponseDto
     */
    @Transactional
    public SignInResponseDto signInGithub(String githubCode) {
        try {
            // 1. Get Access Token
            String gAccessToken = getGithubAccessToken(githubCode);

            // 2. Get User info
            GetGithubUserInfoResponseDto userInfo = getGithubUserInfo(gAccessToken);

            // 3.1 DB 유저 존재확인 -> 있으면 해당 유저 사용
            User user = userRepository.findByLoginId(userInfo.email()).orElseGet(() -> {
                // 3.2 없으면, 새로운 유저 생성
                return this.createUser(
                        LoginType.GITHUB,
                        userInfo.email(),
                        "github",
                        userInfo.login(),
                        userInfo.avatar_url(),
                        0
                );
            });

            // 4. 토큰 페어 발급 -> 리턴
            String acToken = jwtTokenUtil.generateToken(user.getUserId(), JwtTokenUtil.TokenType.ACCESS);
            String rfToken = jwtTokenUtil.generateToken(user.getUserId(), JwtTokenUtil.TokenType.REFRESH);

            return new SignInResponseDto(acToken, rfToken);
        } catch(Exception e) {
            System.out.println(e);
            throw new CustomException(ErrorCode.INVALID_GOOGLE_AUTH_CODE);
        }
    }


    @Transactional
    public long resetPasswordAdmin(String email) {
        return resetPassword(email);
    }


    /**
     * 구글 로그인 - 구글 Access Token 가져오기
     * @return google access token
     */
    private String getGoogleAccessToken(String encodedGoogleCode) {
        // 1. encode google code url을 decrypt
        String decryptedCode = encryptionUtil.decodeUrl(encodedGoogleCode);

        // 2. Body Data
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<GetGoogleAccessTokenRequest> body = new HttpEntity<>(
                new GetGoogleAccessTokenRequest(
                        decryptedCode,
                        GOOGLE_CLIENT_ID,
                        GOOGLE_CLIENT_SECRET,
                        FRONT_REDIRECT_URL,
                        "authorization_code"),
                headers
        );

        try {
            // 3. decrypt된 code값으로 google access token 받아오기 : [POST] https://oauth2.googleapis.com/token
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<GetGoogleAccessTokenResponseDto> response = restTemplate.exchange(
                    GOOGLE_GET_ACCESS_TOKEN_URL,
                    HttpMethod.POST,
                    body,
                    GetGoogleAccessTokenResponseDto.class
            );

            // 4. response 에서 access token 추출 리턴
            return Objects.requireNonNull(response.getBody()).access_token();
        } catch(Exception e) {
            e.getMessage();
            throw new IllegalArgumentException("fail to get google access token");
        }
    }

    /**
     * 구글 로그인 - 계정정보가져오기
     */
    private GetGoogleUserInfoResponseDto getGoogleUserInfo(String googleAccessToken) {
        String requestUrl = GOOGLE_GET_USERINFO_BASE_URL + googleAccessToken;

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GetGoogleUserInfoResponseDto> response = restTemplate.getForEntity(requestUrl, GetGoogleUserInfoResponseDto.class);

        return Objects.requireNonNull(response.getBody());
    }

    /**
     * Github 로그인 - 깃허브 Access Token 가져오기
     */
    private String getGithubAccessToken(String githubCode) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        final HttpEntity<GetGithubAccessTokenRequest> body = new HttpEntity<>(
                new GetGithubAccessTokenRequest(
                        GITHUB_CLIENT_ID,
                        GITHUB_CLIENT_SECRET,
                        githubCode
                ),
                headers
        );

        try {
            // 3. decrypt된 code값으로 google access token 받아오기 : [POST] https://oauth2.googleapis.com/token
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<GetGithubAccessTokenResponse> response = restTemplate.exchange(
                    GITHUB_GET_ACCESS_TOKEN_URL,
                    HttpMethod.POST,
                    body,
                    GetGithubAccessTokenResponse.class
            );

            // 4. response 에서 access token 추출 리턴
            return Objects.requireNonNull(response.getBody()).access_token();
        } catch(Exception e) {
            e.getMessage();
            throw new IllegalArgumentException("fail to get github access token");
        }
    }

    /**
     * Github 로그인 - 계정정보 가져오기
     */
    private GetGithubUserInfoResponseDto getGithubUserInfo(String githubAccessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubAccessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GetGithubUserInfoResponseDto> response = restTemplate.exchange(
                GITHUB_GET_USERINFO_BASE_URL,
                HttpMethod.GET,
                request,
                GetGithubUserInfoResponseDto.class
        );

        return Objects.requireNonNull(response.getBody());
    }

    /**
     * 유저 중복 체크
     */
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByLoginId(email);
    }

    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }


    /**
     * Reset Password
     */
    private long resetPassword(String email) {
        User user = userRepository.findByLoginId(email).orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGINID));

        String encryptedUserPw = encryptionUtil.encryptBcrypt("123456");
        user.setNewPassword(encryptedUserPw);

        userRepository.save(user);

        return user.getUserId();
    }

    /**
     * 유저생성
     * return 생성시간
     */
    private User createUser(
            LoginType loginType,
            String loginId,
            String loginPw,
            String nickname,
            String profileImg,
            int initScore
    ) {
        User user = User.builder()
                        .loginType(loginType)
                        .loginId(loginId)
                        .loginPw(loginPw)
                        .nickname(nickname)
                        .profileImg(profileImg)
                        .initScore(initScore)
                        .score(initScore)
                        .status(UserStatus.ACTIVE)
                        .role(1)
                        .build();

        return userRepository.save(user);
    }
}
