package com.teamnine.noFreeRider.member.service;

import com.teamnine.noFreeRider.auth.JwtTokenProvider;
import com.teamnine.noFreeRider.auth.TokenInfo;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.RefreshToken;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.Member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public Member addUser(String userName, String email, String password) {
        Member member = new Member(userName, email, passwordEncoder.encode(password));
        return memberRepository.save(member);
    }

    @Transactional
    public TokenInfo login(String email, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder
                .getObject().authenticate(authenticationToken);

        Optional<Member> member = memberRepository.findByMemberEmail(email);

        if (member.isEmpty()) {
            throw new AccessDeniedException("not found user");
        }

        // 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, email);

        // refresh token 없으면 생성 or update
        refreshTokenRepository.findByMember_memberEmail(member.get().getMemberEmail())
                .ifPresentOrElse(refreshToken -> {
                    refreshToken.setRefreshToken(tokenInfo.refreshToken());
                    refreshTokenRepository.save(refreshToken);
                }, () -> {
                    refreshTokenRepository.save(new RefreshToken(tokenInfo.refreshToken(), member.get()));
                });

        return tokenInfo;
    }
}
