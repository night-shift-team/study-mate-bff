package com.studyMate.studyMate.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {
    @Email(message = "invalid email")
    String loginId;

    @NotBlank(message = "invalid password")
    @Pattern(
            regexp = "^(?=.*[A-Z!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,}$",
            message = "비밀번호는 6자 이상이며 대문자 또는 특수문자를 포함해야 합니다"
    )
    String loginPw;

    String nickname;
}
