package com.teamnine.noFreeRider.comments.service;

import com.teamnine.noFreeRider.comments.domain.RatingInvite;
import com.teamnine.noFreeRider.comments.repository.RatingInviteRedisRepository;
import com.teamnine.noFreeRider.project.domain.Invite;
import com.teamnine.noFreeRider.project.dto.AcceptInviteDto;
import com.teamnine.noFreeRider.project.repository.InviteRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RatingInviteService {
    private final RatingInviteRedisRepository ratingInviteRedisRepository;

    public UUID create(UUID projectId) {
        RatingInvite ratingInvite = new RatingInvite(projectId);
        ratingInviteRedisRepository.save(ratingInvite);
        return ratingInvite.getInviteCode();
    }

    public UUID doneRating(UUID ratingCode) {
        Optional<RatingInvite> inviteBox = ratingInviteRedisRepository.findRatingInviteByInviteCode(ratingCode);
        RatingInvite ratingInvite = inviteBox.orElseThrow(() -> new NoSuchElementException("존재하지 않는 초대코드 입니다."));

        if (ratingInvite.isHasRated()) {
            throw new IllegalArgumentException("이미 평가 완료한 프로젝트입니다.");
        }
        ratingInvite.use();
        ratingInviteRedisRepository.save(ratingInvite);
        return ratingInvite.getProjectId();
    }

    public boolean isRated(UUID ratingCode) {
        Optional<RatingInvite> inviteBox = ratingInviteRedisRepository.findRatingInviteByInviteCode(ratingCode);
        RatingInvite ratingInvite = inviteBox.orElseThrow(() -> new NoSuchElementException("존재하지 않는 초대코드 입니다."));
        return ratingInvite.isHasRated();
    }

    public UUID getProjectId(UUID ratingCode) {
        Optional<RatingInvite> inviteBox = ratingInviteRedisRepository.findRatingInviteByInviteCode(ratingCode);
        RatingInvite ratingInvite = inviteBox.orElseThrow(() -> new NoSuchElementException("존재하지 않는 초대코드 입니다."));
        return ratingInvite.getProjectId();
    }
}
