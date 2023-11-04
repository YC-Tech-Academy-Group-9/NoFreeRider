package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.project.dto.MemberProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class MemberProjectService {

    private final MemberProjectRepository memberProjectRepository;

    public boolean isMemberPartInProject(MemberProjectDto dto) {
        return memberProjectRepository.existsByMember_idAndProject_id(dto.member_id(), dto.project_id());
    }

    @Transactional
    public Long deleteMemberProject(MemberProjectDto dto) {
        Long delete_id = memberProjectRepository.findIdByMember_idAndProject_id(dto.member_id(), dto.project_id());
        memberProjectRepository.deleteById(delete_id);
        return delete_id;
    }
}
