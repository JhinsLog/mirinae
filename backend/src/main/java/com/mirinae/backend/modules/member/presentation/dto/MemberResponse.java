package com.mirinae.backend.modules.member.presentation.dto;

import com.mirinae.backend.modules.member.domain.Member;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MemberResponse(
        UUID id,
        String providerId,
        String nickname,
        String role,
        OffsetDateTime createdAt
) {

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getProviderId(),
                member.getNickname(),
                member.getRole().name(),
                member.getCreatedAt()
        );
    }
}
