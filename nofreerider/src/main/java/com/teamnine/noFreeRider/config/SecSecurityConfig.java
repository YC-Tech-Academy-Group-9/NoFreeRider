package com.teamnine.noFreeRider.config;

import com.teamnine.noFreeRider.auth.AuthSuccessHandler;
import com.teamnine.noFreeRider.auth.JwtAuthFilter;
import com.teamnine.noFreeRider.member.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthSuccessHandler authSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // 정적 자원에 대해서 Security를 적용하지 않음으로 설정
//        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .formLogin().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/", "/login", "/signup", "/members/auth/**",
                        "/h2-console/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).oauth2Login()
                .loginPage("/")
                .successHandler(authSuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        return http.build();
    }
}
