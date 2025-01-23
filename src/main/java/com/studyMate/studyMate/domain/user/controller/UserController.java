package com.studyMate.studyMate.domain.user.controller;

import com.studyMate.studyMate.domain.user.dto.SignUpRequestDto;
import com.studyMate.studyMate.domain.user.dto.SignUpResponseDto;
import com.studyMate.studyMate.domain.user.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up/local")
    public SignUpResponseDto signUp(@RequestBody SignUpRequestDto signUpRequestBody){
        return userService.signUpLocal(signUpRequestBody);
    }

}