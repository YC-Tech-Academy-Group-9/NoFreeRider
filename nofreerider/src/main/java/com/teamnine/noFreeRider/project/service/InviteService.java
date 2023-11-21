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
        Iterable<Invite> invites = inviteRedisRepository.findAll();
        Invite invite = null;
        for (Invite i : invites) {
            if (i.getProjectId().equals(dto.projectId()) && i.getInviteCode().equals(dto.invitedCode())) {
                invite = i;
                break;
            }
        }

        if (invite == null) {
            throw new NoSuchElementException("존재하지 않는 초대코드 입니다.");
        }
        if (invite.isInvited()) {
            throw new IllegalArgumentException("이미 사용된 초대코드 입니다.");
        }
        invite.use();
        inviteRedisRepository.save(invite);
        return invite.getProjectId();
    }
}
