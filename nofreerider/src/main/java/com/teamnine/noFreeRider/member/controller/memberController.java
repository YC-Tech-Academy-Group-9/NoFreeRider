package com.teamnine.noFreeRider.member.controller;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.comments.service.CommentService;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.dto.MemberDisplayDto;
import com.teamnine.noFreeRider.member.dto.SignupDto;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class memberController {

    private final MemberService memberService;

//    @PostMapping("/auth/signUp")
//    public ResponseEntity<ResultDto<Member>> signUp(@RequestBody SignupDto signupDto) {
//        Member newMember = memberService.addUser(signupDto.realName(), signupDto.email(), signupDto.studentId(), signupDto.major());
//
//        return ResponseEntity.ok(new ResultDto<>(200, "ok", newMember));
//    }

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentService commentService;
    @PutMapping("/info/update/{memberId}")
    public ResponseEntity<ResultDto<Member>> updateMember(
            @PathVariable("memberId") UUID memberId,
            @RequestBody SignupDto signupDto
    ) {
        try {
            Member member = memberService.updateMember(memberId, signupDto);
            commentService.createComment(member);
            return ResponseEntity.ok(new ResultDto<>(200, "ok", member));

        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409)
                    .body(new ResultDto<>(409, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(new ResultDto<>(404, e.getMessage(), null));
        }

    }

    @GetMapping("/info/{memberId}")
    public ResponseEntity<ResultDto<MemberDisplayDto>> getMember(
            @PathVariable("memberId") UUID memberId
    ) {
        try {
            return getResultDtoResponseEntity(memberId);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("not found memberId")) {
                return ResponseEntity.status(404).body(
                        new ResultDto<>(404, "not found memberId", null)
                );
            }
            return ResponseEntity.badRequest().body(
                    new ResultDto<>(400, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/info/email/{email}")
    public ResponseEntity<ResultDto<MemberDisplayDto>> getMemberByEmail(
            @PathVariable("email") String memberEmail
    ) {
        try {
            UUID memberId = memberRepository.findByMemberEmail(memberEmail).orElseThrow(() -> new IllegalArgumentException("not found memberId")).getMemberId();
            return getResultDtoResponseEntity(memberId);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("not found memberId")) {
                return ResponseEntity.status(404).body(
                        new ResultDto<>(404, "not found memberId", null)
                );
            }
            return ResponseEntity.badRequest().body(
                    new ResultDto<>(400, e.getMessage(), null)
            );
        }
    }

    private ResponseEntity<ResultDto<MemberDisplayDto>> getResultDtoResponseEntity(UUID memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found memberId"));
        UserComment userComment = commentService.getCommentByMember(member);
        double[] criteria = userComment.calculateCriteria();
        MemberDisplayDto memberDisplayDto = new MemberDisplayDto(
                member.getMemberName(),
                member.getMemberEmail(),
                member.getMemberStudentId(),
                member.getMemberMajor(),
                member.getMemberTemperature(),
                criteria[0],
                criteria[1],
                criteria[2],
                criteria[3]
        );
        return ResponseEntity.ok().body(
                new ResultDto<>(200, "ok", memberDisplayDto)
        );
    }

    @GetMapping("/info")
    public ResultDto<Member> getCurrentMemberFromToken(
            @CookieValue(value = "accessToken") String token
    ) {
        String email = memberService.getCurrentMemberEmailFromToken(token);
        Member member = memberService.getMemberByEmail(email);
        return new ResultDto<>(200, "ok", member);
    }




}
