package com.teamnine.noFreeRider.auth;

import com.teamnine.noFreeRider.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class FilterService extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        final MemberRepository memberRepository;
//
//
//        if (request.getRequestURI().equals("/login")) {
//           if (response.)
//        } else {
//            filterChain.doFilter(request, response);
//        }
    }
}
