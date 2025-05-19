package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.AuthRequest;
import com.mongodb.api.hrms.dto.AuthResponse;
import com.mongodb.api.hrms.security.CustomUserDetails;
import com.mongodb.api.hrms.security.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @InjectMocks
    AuthService authService;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtUtils jwtUtils;

    @Test
    void login() {
        // given

        AuthRequest authRequest = new AuthRequest();

        authRequest.setUsername("john.doe");
        authRequest.setPassword("j0hn.d03");

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()
        );

        Authentication fullyPopulatedAuthenticationObject = mock(Authentication.class);

        CustomUserDetails principal = new CustomUserDetails();

        principal.setUsername(authRequest.getUsername());
        principal.setPassword("$2a$10$pZBoTvUViK9b7YBZdo2mJOjJtRnxLZDZYSACw8LLC.ZHdCni.Rui2");
        principal.setRole("ADMIN");

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzQ3NTM4" +
                "NTk4LCJleHAiOjE3NDc1NDIxOTh9.16fKiipBYSGIMkjfFNJQZ2Af6GHF_x3Kw1kTg-Tj0tY";

        AuthResponse authResponse = new AuthResponse(token);

        when(authenticationManager.authenticate(authentication)).thenReturn(fullyPopulatedAuthenticationObject);
        when(fullyPopulatedAuthenticationObject.getPrincipal()).thenReturn(principal);
        when(jwtUtils.generateToken(principal)).thenReturn(token);

        // when

        AuthResponse result = authService.login(authRequest);

        // then

        verify(authenticationManager).authenticate(authentication);
        verify(fullyPopulatedAuthenticationObject).getPrincipal();
        verify(jwtUtils).generateToken(principal);

        assertEquals(authResponse, result);
    }
}
