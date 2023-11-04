package com.teamnine.noFreeRider.member.service;

import com.teamnine.noFreeRider.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByMemberEmail(username)
                .map(member -> User.builder()
                        .username(member.getUsername())
                        .password(member.getPassword())
                        .roles(Arrays.toString(member.getAuthorities().toArray()))
                        .build())
                .orElseThrow(()-> new UsernameNotFoundException("not found user"));
    }
}
