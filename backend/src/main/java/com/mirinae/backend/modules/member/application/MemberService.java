package com.mirinae.backend.modules.member.application;

import com.mirinae.backend.modules.member.domain.Member;
import com.mirinae.backend.modules.member.infrastructure.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public UUID signUp(MemberCommand.SignUp command) {
        // 1. 소셜 로그인 제공처 식별키 중복 여부 검증
        if (memberRepository.findByProviderId(command.providerId()).isPresent()) {
            throw new IllegalArgumentException("이미 등록된 회원입니다.");
        }
        // 2. 익명 닉네임 전역 중복 검증
        if (memberRepository.existsByNickname(command.nickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // 3. 도메인 엔티티 내부 제약 조건(2자 이상, 10자 이하) 검증 및 인스턴스 생성
        Member member = Member.create(command.providerId(), command.nickname());

        // 4. 영속화 처리 후 식별자 반환
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void updateNickname(MemberCommand.UpdateNickname command) {
        //1. 대상 회원 존재 여부 검증 및 영속 상태 획득
        Member member = memberRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        //2. 닉네임 변경 전 후 일치 여부 확인
        if (member.getNickname().equals(command.newNickname())) {
            return;
        }

        //3. 변경 대상 신규 닉네임의 사용 여부 확인
        if (memberRepository.existsByNickname(command.newNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        //4. 회원 도메인
        member.updateNickname(command.newNickname());
    }

    public Member getMember(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}