package com.teamnine.noFreeRider.comments.controller;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.comments.dto.CommentDto;
import com.teamnine.noFreeRider.comments.dto.CommentFormDto;
import com.teamnine.noFreeRider.comments.dto.ReceiveCommentDto;
import com.teamnine.noFreeRider.comments.dto.UpdateCommentDto;
import com.teamnine.noFreeRider.comments.repository.CommentsRepository;
import com.teamnine.noFreeRider.comments.service.CommentService;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentAPIController {

    private final CommentService commentService;
    private final CommentsRepository commentsRepository;
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<Optional<UserComment>> getComment(@PathVariable UUID memberId) {
        Optional<UserComment> comment = commentsRepository.findByMember(memberService.getMemberById(memberId));
        // if comment doesn't exist for the user, create new for that user
        if (comment.isEmpty()) {
            comment = Optional.ofNullable(commentService.createComment(memberService.getMemberById(memberId)));
        }
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/submit")
    public ResponseEntity<ResultDto<CommentDto>> submitComment(@RequestBody CommentFormDto commentFormDto) {
        // loop through commentFormDto and run the updateComment method for each member
        for (ReceiveCommentDto receiveCommentDto : commentFormDto.comments()) {
            Member member = memberService.getMemberById(receiveCommentDto.member());
            commentService.update(new UpdateCommentDto(
                    receiveCommentDto.criteria1(),
                    receiveCommentDto.criteria2(),
                    receiveCommentDto.criteria3(),
                    receiveCommentDto.criteria4()),member
                    );
        }
        return ResponseEntity.ok(new ResultDto<>(200, "ok", null));
    }
}
