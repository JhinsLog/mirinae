package com.mirinae.backend.modules.member.presentation;

import com.mirinae.backend.modules.member.application.MemberCommand;
import com.mirinae.backend.modules.member.application.MemberService;
import com.mirinae.backend.modules.member.domain.Member;
import com.mirinae.backend.modules.member.presentation.dto.MemberNicknameUpdateRequest;
import com.mirinae.backend.modules.member.presentation.dto.MemberResponse;
import com.mirinae.backend.modules.member.presentation.dto.MemberSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

     /* 회원 가입 API
     * @param request 회원 가입 요청 데이터 (providerId, nickname)
     * @return 가입된 회원의 식별자 (UUID)
     */
    @PostMapping("/signup")
    public ResponseEntity<UUID> signUp(@Valid @RequestBody MemberSignUpRequest request) {
        MemberCommand.SignUp command = new MemberCommand.SignUp(
                request.providerId(),
                request.nickname()
        );

        UUID memberId = memberService.signUp(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberId);
    }

    /*
    * 단일 회원 정보 조회 API
    * HTTP GET /api/v1/members/{id}
    *
    * @return 정제된 응답 정보 DTO
    * */
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable UUID id) {
        Member member = memberService.getMember(id);
        return ResponseEntity.ok(MemberResponse.from(member));
    }

    @PatchMapping("/{id}/nickname")
    public ResponseEntity<Void> updateNickname(
            @PathVariable UUID id,
            @Valid @RequestBody MemberNicknameUpdateRequest request
    ) {
        MemberCommand.UpdateNickname command = new MemberCommand.UpdateNickname(
                id,
                request.newNickname()
        );
        memberService.updateNickname(command);
        return ResponseEntity.noContent().build();
    }
}