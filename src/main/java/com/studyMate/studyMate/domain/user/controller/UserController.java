package com.studyMate.studyMate.domain.user.controller;

import com.studyMate.studyMate.domain.user.dto.*;
import com.studyMate.studyMate.domain.user.entity.User;
import com.studyMate.studyMate.domain.user.service.UserService;
import com.studyMate.studyMate.global.config.RoleAuth;
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
@RequestMapping("/api/v1/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("/")
    @RoleAuth
    public GetUserDto getUserInfo(HttpServletRequest request) {
        long userId = (Long) request.getAttribute("userId");
        return userService.getActiveUserById(userId);
    }

    @PostMapping("/sign-up/local")
    public SignUpResponseDto signUp(@RequestBody @Validated SignUpRequestDto signUpRequestBody){
        return userService.signUpLocal(signUpRequestBody);
    }

    @PostMapping("/sign-in/local")
    public SignInResponseDto signIn (@RequestBody @Validated SignInRequestDto signInRequestBody) {
        return userService.signInLocal(signInRequestBody);
    }

    @PostMapping("/sign-in/google")
    public SignInResponseDto googleSignIn (@RequestBody @Validated GoogleSignInRequestDto googleSignInRequestBody) {
        return userService.signInGoogle(googleSignInRequestBody.googleCode());
    }

    @GetMapping("/email/duplicate")
    public boolean duplicateCheckEmail(@RequestParam("email") String email){
        return userService.checkDuplicateEmail(email);
    }

    @GetMapping("/nickname/duplicate")
    public boolean duplicateCheckNickname(@RequestParam("nickname") String nickname){
        return userService.checkDuplicateNickname(nickname);
    }

}