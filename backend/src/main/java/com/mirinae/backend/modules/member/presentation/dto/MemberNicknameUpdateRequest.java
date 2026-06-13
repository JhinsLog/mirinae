package com.mirinae.backend.modules.member.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberNicknameUpdateRequest(
        @NotBlank(message = "변경하고자 하는 신규 익명 닉네임은 필수 입력 값입니다.")
        @Size(min = 2, max = 10, message = "닉네임은 최소 2자 이상, 최대 10자 이하로 구성해야 합니다.")
        String newNickname
) {
}
