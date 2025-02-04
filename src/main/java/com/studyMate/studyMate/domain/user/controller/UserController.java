package com.studyMate.studyMate.domain.user.controller;

import com.studyMate.studyMate.domain.user.dto.SignInRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignInResponseDto;
import com.studyMate.studyMate.domain.user.dto.SignUpRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignUpResponseDto;
import com.studyMate.studyMate.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up/local")
    public SignUpResponseDto signUp(@RequestBody @Validated SignUpRequestDto signUpRequestBody){
        return userService.signUpLocal(signUpRequestBody);
    }

    @PostMapping("/sign-in/local")
    public SignInResponseDto signIn (@RequestBody @Validated SignInRequestDto signInRequestBody) {
        return userService.signInLocal(signInRequestBody);
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