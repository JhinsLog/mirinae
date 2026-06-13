package com.mirinae.backend.modules.member.infrastructure;

import com.mirinae.backend.modules.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    /*
    * OAuth 2.0 제공처의 고유 식별자로 기존 등록된 회원을 조회
    * @param providerId OAuth 2.0 제공처의 고유 식별자
    * @return 회원 객체 (Optional)
    */
    Optional<Member> findByProviderId(String providerId);

    /*
    * 해당 닉네임이 사용 중인지 여부를 검사
    * @param nickname 검사할 닉네임
    * @return 사용 중인 경우 true, 사용 가능한 경우 false
    */
    boolean existsByNickname(String nickname);
}
