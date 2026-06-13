package com.mirinae.backend.modules.member.application;

import java.util.UUID;

public class MemberCommand {

    public record SignUp(
            String providerId,
            String nickname
    ) {
        public SignUp {
            if (providerId == null || providerId.strip().isEmpty()) {
                throw new IllegalArgumentException("OAuth 인증 제공처 식별키는 필수 값 입니다.");
            }
            if (nickname == null || nickname.strip().isEmpty()) {
                throw new IllegalArgumentException("닉네임은 필수 값 입니다.");
            }
            if (nickname.length() < 2 || nickname.length() > 10) {
                throw new IllegalArgumentException("닉네임은 10자 이하로 입력해야 합니다.");
            }
        }
    }

    public record UpdateNickname(
            UUID id,
            String newNickname
    ) {
        public UpdateNickname {
            if (id == null) {
                throw new IllegalArgumentException("회원 식별자는 필수 값입니다.");
            }
            if (newNickname == null || newNickname.strip().isEmpty()) {
                throw new IllegalArgumentException("변경할 닉네임은 필수 값입니다.");
            }
            if (newNickname.length() < 2 || newNickname.length() > 10) {
                throw new IllegalArgumentException("닉네임은 2자 이상 10자 이하로 입력해야 합니다.");
            }
        }
    }
}