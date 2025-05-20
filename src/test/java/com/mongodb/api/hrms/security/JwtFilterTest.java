package com.mongodb.api.hrms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {
    @InjectMocks
    JwtFilter jwtFilter;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    UserDetailsService userDetailsService;

    @Test
    void doFilterInternal() throws ServletException, IOException {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            FilterChain filterChain = mock(FilterChain.class);

            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzQ3" +
                    "NTM4NTk4LCJleHAiOjE3NDc1NDIxOTh9.16fKiipBYSGIMkjfFNJQZ2Af6GHF_x3Kw1kTg-Tj0tY";
            String username = "john.doe";
            String password = "$2a$10$nVbfmJDaDfE/BHELmkrN4ez3Dsttrs8cjLNEHkMzQeL.7xR.oSGse";
            String role = "ADMIN";
            CustomUserDetails customUserDetails = new CustomUserDetails(username, password, role);
            List<String> roles = List.of("ROLE_" + role);
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, authorities);

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtUtils.extractUsername(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userDetailsService.loadUserByUsername(username)).thenReturn(customUserDetails);
            when(jwtUtils.extractRoles(token)).thenReturn(roles);

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(request).getHeader("Authorization");
            verify(jwtUtils).extractUsername(token);
            verify(securityContext).getAuthentication();
            verify(userDetailsService).loadUserByUsername(username);
            verify(jwtUtils).extractRoles(token);
            verify(securityContext).setAuthentication(authentication);
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternalWithNullAuth() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(jwtUtils);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternalWithoutBearerToken() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Hi!");

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(filterChain).doFilter(request, response);

        verifyNoInteractions(jwtUtils);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void doFilterInternalWithNullUser() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzQ3NTM4" +
                "NTk4LCJleHAiOjE3NDc1NDIxOTh9.16fKiipBYSGIMkjfFNJQZ2Af6GHF_x3Kw1kTg-Tj0tY";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtils.extractUsername(token)).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        verify(request).getHeader("Authorization");
        verify(jwtUtils).extractUsername(token);
        verify(filterChain).doFilter(request, response);

        verify(userDetailsService, times(0)).loadUserByUsername(any());
        verify(jwtUtils, times(0)).extractRoles(any());
    }

    @Test
    void doFilterInternalWithAuthenticatedUser() throws ServletException, IOException {
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            FilterChain filterChain = mock(FilterChain.class);

            String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzQ3" +
                    "NTM4NTk4LCJleHAiOjE3NDc1NDIxOTh9.16fKiipBYSGIMkjfFNJQZ2Af6GHF_x3Kw1kTg-Tj0tY";
            String username = "john.doe";
            String password = "$2a$10$nVbfmJDaDfE/BHELmkrN4ez3Dsttrs8cjLNEHkMzQeL.7xR.oSGse";
            String role = "ADMIN";
            CustomUserDetails customUserDetails = new CustomUserDetails(username, password, role);
            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, authorities);

            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtUtils.extractUsername(token)).thenReturn(username);
            when(securityContext.getAuthentication()).thenReturn(authentication);

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(request).getHeader("Authorization");
            verify(jwtUtils).extractUsername(token);
            verify(securityContext).getAuthentication();
            verify(filterChain).doFilter(request, response);

            verify(userDetailsService, times(0)).loadUserByUsername(any());
            verify(jwtUtils, times(0)).extractRoles(any());
            verify(securityContext, times(0)).setAuthentication(any());
        }
    }
}
