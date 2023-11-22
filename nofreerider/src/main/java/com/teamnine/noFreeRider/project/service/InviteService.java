package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.project.domain.Invite;
import com.teamnine.noFreeRider.project.dto.AcceptInviteDto;
import com.teamnine.noFreeRider.project.repository.InviteRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class InviteService {
    private final InviteRedisRepository inviteRedisRepository;

    public UUID create(UUID projectId) {
        Invite invite = new Invite(projectId);
        inviteRedisRepository.save(invite);
        return invite.getInviteCode();
    }

    public UUID useCode(AcceptInviteDto dto) {
        Optional<Invite> inviteBox = inviteRedisRepository.findInviteByProjectIdAndInviteCode(dto.projectId(), dto.invitedCode());
        Invite invite = inviteBox.orElseThrow(() -> new NoSuchElementException("존재하지 않는 초대코드 입니다."));

        if (invite.isInvited()) {
            throw new IllegalArgumentException("이미 사용된 초대코드 입니다.");
        }
        invite.use();
        inviteRedisRepository.save(invite);
        return invite.getProjectId();
    }
}
