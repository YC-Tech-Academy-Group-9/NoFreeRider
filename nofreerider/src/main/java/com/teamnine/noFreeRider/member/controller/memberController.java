package com.teamnine.noFreeRider.member.controller;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.dto.SignupDto;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

    @PostMapping("/auth/signUp")
    public ResponseEntity<ResultDto<Member>> signUp(@RequestBody SignupDto signupDto) {
        Member newMember = memberService.addUser(signupDto.userName(), signupDto.email(), signupDto.studentId(), signupDto.major());

        return ResponseEntity.ok(new ResultDto<>(200, "ok", newMember));
    }

    @GetMapping("/info/{memberId}")
    public ResultDto<Member> getMember(
            @PathVariable("memberId") UUID memberId
    ) {
        Member member = memberService.getMemberById(memberId);
        return new ResultDto<>(200, "ok", member);
    }
}
