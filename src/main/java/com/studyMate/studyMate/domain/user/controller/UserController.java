package com.studyMate.studyMate.domain.user.controller;

import com.studyMate.studyMate.domain.user.dto.*;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.service.UserService;
import com.studyMate.studyMate.global.config.RoleAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "User API")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/refresh")
    @Operation(summary = "Token 리프레쉬(*)", description = "토큰 리프레시 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignUpResponseDto.class))})
    })
    @RoleAuth
    public SignInResponseDto tokenRefresh(HttpServletRequest request, @RequestBody @Validated RefreshTokenRequestDto refreshTokenRequestbody) {
        long userId = (Long) request.getAttribute("userId");
        return userService.refreshTokenPair(userId, refreshTokenRequestbody.refreshToken());
    }


    @GetMapping("/oauth/parameters/admin")
//    @RoleAuth(requiredRole = 7)
    @Operation(summary = "OAuth 인자 확인 (어드민 전용)", description = "OAuth 인자확인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = GetOAuthParametersResponseDto.class))})
    })
    public GetOAuthParametersResponseDto getOAuthParameters() {
        return userService.getOauthParameters();
    }

    @GetMapping("/")
    @RoleAuth
    @Operation(summary = "유저정보조회 (*)", description = "유저정보를 조회하는 API (User Score =0 인 경우 Level테스트 진행하지 않은 유저 | 이미 진행한 유저)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = GetUserDto.class))})
    })
    public GetUserDto getUserInfo(HttpServletRequest request) {
        long userId = (Long) request.getAttribute("userId");
        return userService.getActiveUserById(userId);
    }

    @PostMapping("/sign-up/local")
    @Operation(summary = "로컬 회원가입", description = "로컬 회원가입 API")
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

    @PostMapping("/reset-password/admin")
    @RoleAuth(requiredRole = 7)
    @Operation(summary = "비밀번호 초기화 (어드민 전용)", description = "비밀번호 초기화 API (123456)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public long resetPassword (@RequestBody @Validated ResetPasswordRequestDto resetPasswordRequestBody) {
        return userService.resetPasswordAdmin(resetPasswordRequestBody.email());
    }

    @PostMapping("/sign-in/google")
    @Operation(summary = "구글 로그인", description = "구글 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public SignInResponseDto googleSignIn (@RequestBody @Validated GoogleSignInRequestDto googleSignInRequestBody) {
        return userService.signInGoogle(googleSignInRequestBody.googleCode());
    }


    @PostMapping("/sign-in/github")
    @Operation(summary = "깃허브 로그인", description = "깃허브 로그인 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(schema = @Schema(implementation = SignInResponseDto.class))})
    })
    public SignInResponseDto githubSignIn (@RequestBody @Validated GithubSignInRequestDto githubSignINRequestBody) {
        return userService.signInGithub(githubSignINRequestBody.githubCode());
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