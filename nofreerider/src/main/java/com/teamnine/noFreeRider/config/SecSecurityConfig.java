package com.teamnine.noFreeRider.config;

import com.teamnine.noFreeRider.auth.AuthSuccessHandler;
import com.teamnine.noFreeRider.auth.JwtAuthFilter;
import com.teamnine.noFreeRider.member.service.CustomOAuth2UserService;
import com.teamnine.noFreeRider.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                .antMatchers("/", "/members/auth/**",
                        "/h2-console/**", "/logout")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).oauth2Login()
                .loginPage("/")
                .successHandler(authSuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and().and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    // Custom logout logic (if needed)
                    response.setStatus(HttpServletResponse.SC_OK);
                    CookieUtil.deleteCookie(request, response, "accessToken");
                    CookieUtil.deleteCookie(request, response, "refreshToken");
                    response.getWriter().write("Logged out successfully");
                });;

        return http.build();
    }
}
