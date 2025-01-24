package com.studyMate.studyMate.domain.user.service;

import com.studyMate.studyMate.domain.user.data.LoginType;
import com.studyMate.studyMate.domain.user.data.UserStatus;
import com.studyMate.studyMate.domain.user.dto.SignInRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignInResponseDto;
import com.studyMate.studyMate.domain.user.dto.SignUpRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignUpResponseDto;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.repository.UserRepository;
import com.studyMate.studyMate.global.error.CustomException;
import com.studyMate.studyMate.global.error.ErrorCode;
import com.studyMate.studyMate.global.util.EncryptionUtil;
import com.studyMate.studyMate.global.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final EncryptionUtil encryptionUtil;
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
        User user = userRepository.findByLoginId(signInRequestDto.loginId());
        if(user == null){
            throw new CustomException(ErrorCode.INVALID_LOGINID);
        }

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
