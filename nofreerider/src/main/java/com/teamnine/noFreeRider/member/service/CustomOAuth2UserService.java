package com.teamnine.noFreeRider.member.service;

import com.teamnine.noFreeRider.member.domain.Member;
import com.teamnine.noFreeRider.member.dto.OAuth2Attribute;
import com.teamnine.noFreeRider.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService
                = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute
                = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> memberAttribute = oAuth2Attribute.convert2Map();

        String email = (String) memberAttribute.getOrDefault("email", "");
        Optional<Member> member = memberRepository.findByMemberEmail(email);


        if (member.isEmpty()) {
            memberAttribute.put("exist", false);
        } else {
            memberAttribute.put("exist", true);
        }

        if (member.get().isFullySet()) {
            memberAttribute.put("initialized", true);
        } else {
            memberAttribute.put("initialized", false);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email"
        );
    }
}