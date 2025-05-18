package com.studyMate.studyMate.domain.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequestDto {
    @Email(message = "invalid email")
    private String loginId;
    private String loginPw;
}
