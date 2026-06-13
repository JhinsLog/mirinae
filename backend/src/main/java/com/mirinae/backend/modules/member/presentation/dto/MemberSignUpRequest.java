package com.mirinae.backend.modules.member.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest(
        @NotBlank(message = "OAuth 제공자 식별 키는 필수 입력 값입니다.")
        String providerId,

        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해야 합니다.")
        String nickname
) {
}
