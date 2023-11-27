package com.teamnine.noFreeRider.project.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.MemberProject;
import com.teamnine.noFreeRider.member.repository.MemberProjectRepository;
import com.teamnine.noFreeRider.member.service.MemberService;
import com.teamnine.noFreeRider.project.domain.Project;
import com.teamnine.noFreeRider.project.dto.MemberProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberProjectService {

    private final MemberService memberService;
    private final MemberProjectRepository memberProjectRepository;

    public boolean isMemberPartInProject(MemberProjectDto dto) {
        return memberProjectRepository.existsByMemberIdAndProjectId(dto.member_id(), dto.project_id());
    }

    public boolean isMemberPartInProject(UUID memberId, UUID projectId) {
        return memberProjectRepository.existsByMemberIdAndProjectId(memberId, projectId);
    }

    @Transactional
    public Long deleteMemberProject(MemberProjectDto dto) {
        MemberProject memberProject = memberProjectRepository.findByMemberIdAndProjectId(dto.member_id(), dto.project_id())
                .orElseThrow(IllegalArgumentException::new);
        Long delete_id = memberProject.getId();
        memberProjectRepository.deleteById(delete_id);
        return delete_id;
    }

    public List<Project> getProjectListByMember(Member member) {
        List<MemberProject> memberProjects = memberProjectRepository.findAllByMemberId(member.getId()).orElseThrow();
        return memberProjects.stream().map(MemberProject::getProject).toList();
    }

    public List<Member> getMemberListByProject(Project project) {
        List<MemberProject> memberProjects = memberProjectRepository.findAllByProjectId(project.getId()).orElseThrow();
        //reorder so that leader is first
        Member leader = memberProjects.stream().filter(memberProject -> memberProject.getMember().getId().equals(project.getLeader().getId())).findFirst().orElseThrow().getMember();
        memberProjects.removeIf(memberProject -> memberProject.getMember().getId().equals(project.getLeader().getId()));
        memberProjects.add(0, new MemberProject(leader, project));
        return memberProjects.stream().map(MemberProject::getMember).toList();

    }

    public List<MemberProject> getMemberProjectListByProject(Project project) {
        return memberProjectRepository.findAllByProjectId(project.getId()).orElseThrow();
    }
}
