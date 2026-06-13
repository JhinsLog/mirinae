package com.mirinae.backend.modules.member.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "provider_id", nullable = false, unique = true, length = 255)
    private String providerId;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private MemberRole role;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.role == null) {
            this.role = MemberRole.USER; // 기본 역할 설정
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }


    public static Member create(String providerId, String nickname) {
        validateProviderId(providerId);
        validateNickname(nickname);

        return Member.builder()
                .providerId(providerId)
                .nickname(nickname)
                .role(MemberRole.USER)
                .build();
    }

    public void updateNickname(String newNickname) {
        validateNickname(newNickname);
        this.nickname = newNickname;
    }

    public void changeRole(MemberRole newRole) {
        if (newRole == null) {
            throw new IllegalArgumentException("권한 등급 정보는 null 일 수 없습니다.");
        }
        this.role = newRole;
    }


    /*내부 도메인 유효성 검증 로직*/

    private static void validateProviderId(String providerId) {
        if (providerId == null || providerId.strip().isEmpty()) {
            throw new IllegalArgumentException("소셜 로그인 제공자 ID는 null 또는 빈 문자열일 수 없습니다.");
        }
    }

    private static void validateNickname(String nickname) {
        if (nickname == null || nickname.strip().isEmpty()) {
            throw new IllegalArgumentException("닉네임은 null 또는 빈 문자열일 수 없습니다.");
        }
        if (nickname.length() > 10) {
            throw new IllegalArgumentException("닉네임은 10자 이하로 입력해야 합니다.");
        }
    }


    /*회원 권한 등급 체계 Enum*/

    public enum MemberRole {
        USER, ADMIN, TESTER
    }
}
