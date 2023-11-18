package com.teamnine.noFreeRider.comments.service;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.comments.repository.CommentsRepository;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final MemberRepository memberRepository;
    private final CommentsRepository commentRepository;
    public UserComment createComment(int userID) {
        UserComment newComment = new UserComment(userID, 0, 0, 0, 0, null, null);
        commentRepository.save(newComment);
        return newComment;
    }

    public UserComment getCommentByUserId(int userID) {
        return commentRepository.findByUserId(userID).orElseThrow(() -> new IllegalArgumentException("not found comment"));
    }

    public UserComment updateComment(UserComment updatedComment) {
        UserComment currComment = commentRepository.findByUserId(updatedComment.getUserId()).orElseThrow(() -> new IllegalArgumentException("not found comment"));
        return currComment;
    }

    public void deleteComment(UserComment userComment) {

    }
}
