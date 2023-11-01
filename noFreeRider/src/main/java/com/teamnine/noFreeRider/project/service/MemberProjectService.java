package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.project.dto.MemberProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberProjectService {

    private final MemberProjectRepository memberProjectRepository;

    public boolean isMemberPartInProject(MemberProjectDto dto) {
        return memberProjectRepository.existsByMember_noAndProject_no(dto.memberId(), dto.projectId());
    }

    public Long deleteMemberProject(MemberProjectDto dto) {
        Long deleteId = memberProjectRepository.findIdByMember_noAndProject_no(dto.memberId(), dto.projectId());
        memberProjectRepository.deleteById(deleteId);
        return deleteId;
    }
}
