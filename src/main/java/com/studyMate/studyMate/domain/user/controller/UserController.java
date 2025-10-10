package com.studyMate.studyMate.domain.user.controller;

import com.studyMate.studyMate.domain.user.dto.*;
import com.studyMate.studyMate.domain.user.service.UserService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User API")
public class UserController {
    private final UserService userService;

    @PostMapping("/refresh")
    @Operation(summary = "Token 리프레쉬(*)", description = "토큰 리프레시 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignUpResponseDto.class))})
    })
    @RoleAuth
    public SignInResponseDto tokenRefresh(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return userService.refreshTokenPair(userId);
    }

    @GetMapping("/")
    @RoleAuth
    @Operation(summary = "유저정보조회 (*)", description = "유저정보를 조회하는 API (User Score =0 인 경우 Level테스트 진행하지 않은 유저 | 이미 진행한 유저)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = GetUserDto.class))})
    })
    public GetUserDto getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        return userService.getActiveUserById(userId);
    }

    @PostMapping("/sign-up/local/email-verification")
    @Operation(summary = "[로컬 회원가입 - 1] 로컬 회원가입 인증 이메일 전송", description = "로컬 회원가입 전, 인증 이메일 전송 API")
    public String signUpLocalEmailSend(@RequestBody @Validated SignUpEmailSendRequestDto signUpEmailSendRequestDto){
        return userService.sendVerificationEmail(signUpEmailSendRequestDto.getEmail());
    }

    @PostMapping("/sign-up/local/email-verification/verify")
    @Operation(summary = "[로컬 회원가입 - 2] 로컬 회원가입 이메일 검증", description = "인증 이메일 코드 검증 API")
    public String signUpLocalEmailVerification(@RequestBody @Validated SignUpVerifyRequestDto signUpVerifyRequestDto){
        return userService.verifyEmailCode(signUpVerifyRequestDto.getEmail(), signUpVerifyRequestDto.getCode());
    }

    @PostMapping("/sign-up/local")
    @Operation(summary = "[로컬 회원가입 - 3] 로컬 회원 가입 등록", description = "로컬 회원가입 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignUpResponseDto.class))})
    })
    public SignUpResponseDto signUp(@RequestBody @Validated SignUpRequestDto signUpRequestBody){
        return userService.signUpLocal(signUpRequestBody);
    }

    @PostMapping("/sign-in/local")
    @Operation(summary = "로컬 로그인", description = "로컬 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public SignInResponseDto signIn (@RequestBody @Validated SignInRequestDto signInRequestBody) {
        return userService.signInLocal(signInRequestBody);
    }

    @PostMapping("/reset-password/email-verification")
    @Operation(summary = "[비밀번호 초기화 - 1] 비밀번호 초기화 이메일 전송", description = "비밀번호 초기화를 위한 인증 코드 메일 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public String resetPasswordEmailVerification (@RequestBody @Validated ResetPasswordEmailVerificationRequestDto resetPasswordEmailVerificationRequestDto) {
        return userService.sendResetPasswordVerificationEmail(resetPasswordEmailVerificationRequestDto.getEmail());
    }

    @PostMapping("/reset-password/email-verification/verify")
    @Operation(summary = "[비밀번호 초기화 - 2] 비밀번호 초기화", description = "비밀번호 초기화 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public String resetPassword (@RequestBody @Validated ResetPasswordRequestDto resetPasswordRequestDto) {
        return userService.resetPassword(resetPasswordRequestDto.getEmail(), resetPasswordRequestDto.getCode());
    }

    @PostMapping("/sign-in/google")
    @Operation(summary = "구글 로그인", description = "구글 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public SignInResponseDto googleSignIn (@RequestBody @Validated GoogleSignInRequestDto googleSignInRequestBody) {
        return userService.signInGoogle(googleSignInRequestBody.getGoogleCode());
    }

    @PostMapping("/change-password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API")
    @RoleAuth
    public String changePassword(@RequestBody @Validated ChangePasswordRequestDto changePasswordRequestDto) {
        return userService.changePassword(changePasswordRequestDto.getLoginId(), changePasswordRequestDto.getOldPassword(), changePasswordRequestDto.getNewPassword());
    }

    @PatchMapping("/nickname")
    @Operation(summary = "유저 닉네임 변경", description = "닉네임 변경 API")
    @RoleAuth
    public String updateUserNickname(
            HttpServletRequest request,
            @RequestBody UpdateUserNicknameRequestDto updateUserNicknameRequestDto
    ) {
        String userId = (String) request.getAttribute("userId");
        return userService.updateUserNickname(userId, updateUserNicknameRequestDto.getNickname());
    }

    @GetMapping("/email/duplicate")
    @Operation(summary = "이메일 중복체크", description = "이메일 중복체크 API (true = 이미 존재함, false = 존재하지 않음)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = boolean.class))})
    })
    public boolean duplicateCheckEmail(@RequestParam("email") String email){
        return userService.checkDuplicateEmail(email);
    }

    @GetMapping("/nickname/duplicate")
    @Operation(summary = "닉네임 중복체크", description = "닉네임 중복체크 API (true = 이미 존재함, false = 존재하지 않음)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = boolean.class))})
    })
    public boolean duplicateCheckNickname(@RequestParam("nickname") String nickname){
        return userService.checkDuplicateNickname(nickname);
    }
}
