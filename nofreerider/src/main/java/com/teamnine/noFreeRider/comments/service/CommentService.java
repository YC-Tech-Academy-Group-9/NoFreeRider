package com.teamnine.noFreeRider.comments.service;

import com.teamnine.noFreeRider.comments.domain.UserComment;
import com.teamnine.noFreeRider.comments.dto.UpdateCommentDto;
import com.teamnine.noFreeRider.comments.repository.CommentsRepository;
import com.teamnine.noFreeRider.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.h2.engine.User;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentsRepository commentRepository;

    @Transactional
    public UserComment createComment(Member member) {
        System.out.println("save comment for memberId: "+member.getMemberEmail());
        UserComment comment = UserComment.builder()
                .member(member)
                .criteria1(0)
                .criteria2(0)
                .criteria3(0)
                .criteria4(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .numUpdates(0)
                .build();
        return commentRepository.save(comment);
    }

    @Transactional
    public void update(UpdateCommentDto updateCommentDto, Member member) {
        UserComment comment = commentRepository.findByMember(member)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found for memberId"));

        comment.updateCommentCriteria(updateCommentDto);
    }


    public void deleteComment(UserComment userComment) {

    }

    public UserComment getCommentByMember(Member member) {
        return commentRepository.findByMember(member).orElseThrow(() -> new IllegalArgumentException("Comment not found for memberId"));
    }

}
