package com.teamnine.noFreeRider.comments.controller;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.comments.dto.CommentDto;
import com.teamnine.noFreeRider.comments.dto.CommentFormDto;
import com.teamnine.noFreeRider.comments.dto.ReceiveCommentDto;
import com.teamnine.noFreeRider.comments.dto.UpdateCommentDto;
import com.teamnine.noFreeRider.comments.repository.CommentsRepository;
import com.teamnine.noFreeRider.comments.service.CommentService;
import com.teamnine.noFreeRider.comments.service.RatingInviteService;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.util.dto.ResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentAPIController {

    private final CommentService commentService;
    private final CommentsRepository commentsRepository;
    private final MemberService memberService;
    private final RatingInviteService ratingInviteService;

    @GetMapping("/{memberId}")
    public ResponseEntity<Optional<UserComment>> getComment(@PathVariable UUID memberId) {
        Optional<UserComment> comment = commentsRepository.findByMember(memberService.getMemberById(memberId));
        // if comment doesn't exist for the user, create new for that user
        if (comment.isEmpty()) {
            comment = Optional.ofNullable(commentService.createComment(memberService.getMemberById(memberId)));
        }
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/submit/{ratingCode}")
    public ResponseEntity<ResultDto<CommentDto>> submitComment(@PathVariable UUID ratingCode, @RequestBody CommentFormDto commentFormDto) {
        // loop through commentFormDto and run the updateComment method for each memberId
        for (ReceiveCommentDto receiveCommentDto : commentFormDto.comments()) {
            Member member = memberService.getMemberById(receiveCommentDto.memberId());
            commentService.update(new UpdateCommentDto(
                    receiveCommentDto.criteria1(),
                    receiveCommentDto.criteria2(),
                    receiveCommentDto.criteria3(),
                    receiveCommentDto.criteria4()),
                    member
                    );
        }
        ratingInviteService.doneRating(ratingCode);
        return ResponseEntity.ok(new ResultDto<>(200, "ok", null));
    }

    @PostMapping("/rating/{ratingCode}")
    public ResponseEntity<ResultDto<String>> getCommentByRatingCode(@PathVariable UUID ratingCode) {
        try {
            if (ratingInviteService.isRated(ratingCode)) {
                return ResponseEntity.status(405).body(
                        new ResultDto<>(405, "이미 평가 완료한 프로젝트입니다.", null)
                );
            }
            return ResponseEntity.ok().body(
                    new ResultDto<>(201, "ok", "/rating/" + ratingCode)
            );
        } catch (Exception e) {
            return ResponseEntity.status(400).body(
                    new ResultDto<>(400, e.getMessage(), null));
        }
    }
}
