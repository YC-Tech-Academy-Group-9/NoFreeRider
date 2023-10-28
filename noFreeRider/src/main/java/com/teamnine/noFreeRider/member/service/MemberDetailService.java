package com.teamnine.noFreeRider.Member.service;

import com.teamnine.noFreeRider.Member.domain.Member;
import com.teamnine.noFreeRider.Member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String member_id) throws UsernameNotFoundException {
        return memberRepository.findByMemberEmail(member_id)
                .orElseThrow(() -> new IllegalArgumentException((member_id)));
    }


}
