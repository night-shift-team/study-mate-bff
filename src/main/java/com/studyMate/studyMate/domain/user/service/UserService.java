package com.studyMate.studyMate.domain.user.service;

import com.studyMate.studyMate.domain.user.data.OAuthType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.dto.*;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.entity.UserOAuth;
import com.studyMate.studyMate.domain.user.repository.UserOAuthRepository;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.util.EncryptionUtil;
import com.studyMate.studyMate.global.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserOAuthRepository userOAuthRepository;

    private final JwtTokenUtil jwtTokenUtil;
    private final EncryptionUtil encryptionUtil;

    private final String GOOGLE_GET_ACCESS_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_GET_USERINFO_BASE_URL = "https://www.googleapis.com/userinfo/v2/me?access_token=";

    private final String DEFAULT_PROFILE_IMAGE = "https://qwrujioanlzrqiiyxser.supabase.co/storage/v1/object/public/study-mate//default_profile.jpg";

    @Value("${e.auth.google_client_id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${e.auth.google_client_secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${e.auth.redirect_url}")
    private String FRONT_REDIRECT_URL;

    /**
     * 유저 조회 (액티브 유저만)
     */
    public GetUserDto getActiveUserById(String id) {
        User user = userRepository.findByUserIdAndStatus(id, UserStatus.ACTIVE).orElseThrow(() ->  new CustomException(ErrorCode.NOT_ACTIVE_USER));

        List<UserOAuthDto> userOAuthDtos = user.getUserOAuths().stream()
                .map(UserOAuthDto::new)
                .toList();

        return GetUserDto.builder()
                .userId(user.getUserId())
                .loginId(user.getLoginId())
                .nickname(user.getNickname())
                .profileImg(user.getProfileImg())
                .status(user.getStatus())
                .role(user.getRole())
                .userScore(user.getScore())
                .registeredAt(user.getCreatedDt())
                .userOAuth(userOAuthDtos)
                .build();
    }

    /**
     * OAuth Parameter 조회 method for clinet developers
     */
    public GetOAuthParametersResponseDto getOauthParameters() {
        return GetOAuthParametersResponseDto.builder()
                .googleClientId(this.GOOGLE_CLIENT_ID)
                .googleClientSecret(this.GOOGLE_CLIENT_SECRET)
                .googleRedirectUrl(this.FRONT_REDIRECT_URL)
                .build();
    }

    /**
     * Token Refresh
     */
    public SignInResponseDto refreshTokenPair(String userId, String refreshToken) {
        boolean isValid = jwtTokenUtil.validateToken(refreshToken);
        if(!isValid) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        User user = userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE).orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGINID));

        return createTokenPair(user.getUserId());
    }

    /**
     * 로컬 회원가입 메소드
     */
    @Transactional
    public SignUpResponseDto signUpLocal(SignUpRequestDto signUpRequestDto) {
        // 1. 닉네임 확인
        boolean isNicknameValid = checkDuplicateNickname(signUpRequestDto.getNickname());
        if(isNicknameValid){
            throw new CustomException(ErrorCode.DUP_USER_NICKNAME);
        }

        // 2. 기존 사용자 조회
        Optional<User> existingUser = userRepository.findByLoginId(signUpRequestDto.getLoginId());
        String encryptedUserPw = encryptionUtil.encryptBcrypt(signUpRequestDto.getLoginPw());

        // 유저가 없다면 (신규유저) -> 생성 리턴
        if (existingUser.isEmpty()) {
            User newUser = createUser(signUpRequestDto.getLoginId(), encryptedUserPw, signUpRequestDto.getNickname(), DEFAULT_PROFILE_IMAGE, 0);
            return new SignUpResponseDto(newUser.getUserId(), newUser.getLoginId());
        } else {
            User user = processRegisterUser(existingUser.get(), encryptedUserPw);
            return new SignUpResponseDto(user.getUserId(), user.getLoginId());
        }
    }

    /**
     * OAuth만 있는 사용자인지, 중복으로 로컬 회원가입하는것인지 판별하는 메소드
     */
    private User processRegisterUser(User existingUser, String encryptedUserPw) {
        if (isOAuthOnlyUser(existingUser)) {
            // 유저가 있다면 / 비밀번호 판별 oauth면 -> oauth only 사용자 -> local password 업데이트 시키고 리턴
            existingUser.setUserPassword(encryptedUserPw);
            return existingUser;
        } else {
            // 유저가 있다면 / 비밀번호 판별 oauth 아니면 -> 이미 가입한 사용자 -> 중복가입으로 throw
            throw new CustomException(ErrorCode.DUP_USER_ID);
        }
    }

    /**
     * OAtuh 만 가입한 유저인지 확인하는 메소드
     */
    private boolean isOAuthOnlyUser(User user) {
        return "oauth".equals(user.getLocalLoginPw());
    }

    public SignInResponseDto signInLocal(SignInRequestDto SignInRequestDto) {
        // 1. Login Id로 유저 확인
        User user = userRepository.findByLoginId(SignInRequestDto.getLoginId()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_LOGINID));

        // 2. 비밀번호 검증 비교
        boolean isValidPw = encryptionUtil.compareBcrypt(SignInRequestDto.getLoginPw(), user.getLocalLoginPw());
        if(!isValidPw){
            throw new CustomException(ErrorCode.INVALID_LOGINPW);
        }

        // 3. JWT Token 발급
        return createTokenPair(user.getUserId());
    }

    @Transactional
    public String updateUserNickname(String userId, String nickname) {
        User user = userRepository.findByUserIdAndStatus(userId, UserStatus.ACTIVE).orElseThrow(() -> new CustomException(ErrorCode.NOT_ACTIVE_USER));
        if(
                user.getNickname().equals(nickname) ||
                checkDuplicateNickname(nickname)
        ){
            throw new CustomException(ErrorCode.DUP_NICKNAME);
        }

        user.setNewNickname(nickname);
        return user.getUserId();
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

            // (TODO : User 테이블에 존재확인, 있으면 Oauth 목록 등록 확인 -> 있으면 로그인 || 없으면 새로운 OAuth 등록 후 -> 로그인)
            // User 테이블에 존재하지않는다면, User Table에 생성 -> OAuth 목록에 등록 -> 로그인
            // 3.1 DB 유저 존재확인 -> 있으면 해당 유저 사용
            User user = userRepository.findByLoginId(userInfo.getEmail()).orElseGet(() -> {
                // 3.2 없으면, 새로운 유저 생성
                User newUser = this.createUser(
                        userInfo.getEmail(),
                        "oauth",
                        userInfo.getName(),
                        userInfo.getPicture(),
                        0
                );

                Optional<UserOAuth> userOAuth = userOAuthRepository.findByUser(newUser);

                if(userOAuth.isEmpty()){
                    UserOAuth newUserOauth = UserOAuth.builder().user(newUser).oauthType(OAuthType.GOOGLE).build();
                    userOAuthRepository.save(newUserOauth);
                }

                return newUser;
            });

            // 4. 토큰 페어 발급 -> 리턴
            return createTokenPair(user.getUserId());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_GOOGLE_AUTH_CODE);
        }
    }


    public GetUserRankingResponseDto findUserRanking(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        System.out.println("Check UserId");
        return userRepository.findUsersAndRanking(userId, pageRequest);
    }

    /**
     * Token Pair 생성 (Access Token & Refresh Token)
     */
    private SignInResponseDto createTokenPair(String userId) {
        String acToken = jwtTokenUtil.generateToken(userId, JwtTokenUtil.TokenType.ACCESS);
        String rfToken = jwtTokenUtil.generateToken(userId, JwtTokenUtil.TokenType.REFRESH);
        return SignInResponseDto.builder().accessToken(acToken).refreshToken(rfToken).build();
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
            return Objects.requireNonNull(response.getBody()).getAccess_token();
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
     * 유저 중복 체크
     */
    public boolean checkDuplicateEmail(String email) {
        return userRepository.existsByLoginId(email);
    }

    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    /**
     * 유저생성
     * return 생성시간
     */
    private User createUser(
            String loginId,
            String loginPw,
            String nickname,
            String profileImg,
            int initScore
    ) {
        User user = User.builder()
                        .loginId(loginId)
                        .localLoginPw(loginPw)
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
