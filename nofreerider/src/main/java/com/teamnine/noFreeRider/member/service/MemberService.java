package com.teamnine.noFreeRider.member.service;

import com.teamnine.noFreeRider.auth.JwtTokenProvider;
import com.teamnine.noFreeRider.auth.TokenInfo;
import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.domain.RefreshToken;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import com.teamnine.noFreeRider.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    EntityManager entityManager;

    public Member addUser(String userName, String email, int studentId, String major) {


        // 1. Member 객체 생성, PasswordEncoder 로 비밀번호 암호화, 권한은 USER로 설정
        Member newMember = new Member(userName, email, studentId, major);

        // 2. repository 에 저장
        memberRepository.save(newMember);

        // 3. Comment Initialize

        return newMember;
    }

    public Member getMemberById(UUID id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found member"));
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByMemberEmail(email).orElseThrow(() -> new IllegalArgumentException("not found member"));
    }

    @Transactional
    public TokenInfo login(String email, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication, email);

        // 4. email 로 사용자 조회
        Optional<Member> member = memberRepository.findByMemberEmail(email);

        // 5. 사용자가 없는 경우 AccessDeniedException 발생
        if (member.isEmpty()) {
            throw new AccessDeniedException("not found user");
        }

        // 6. refresh token 업데이트 or 생성
        refreshTokenRepository.findByMember_memberEmail(member.get().getMemberEmail())
                .ifPresentOrElse(
                        refreshToken -> {
                            refreshToken.setRefreshToken(tokenInfo.refreshToken());
                            refreshTokenRepository.save(refreshToken);
                        }, () -> refreshTokenRepository.save(new RefreshToken(tokenInfo.refreshToken(), member.get()))
                );

        return tokenInfo;
    }

    public Member getMemberByEmail(String memberEmail) {

        return memberRepository.findByMemberEmail(memberEmail).orElseThrow(() -> new IllegalArgumentException("not found member"));
    }

    public String getCurrentMemberEmailFromToken(String token) {
        return jwtTokenProvider.getUserEmailFromToken(token);
    }
}
