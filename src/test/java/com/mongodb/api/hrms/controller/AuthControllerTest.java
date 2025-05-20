package com.mongodb.api.hrms.controller;

import com.mongodb.api.hrms.dto.AuthRequest;
import com.mongodb.api.hrms.dto.AuthResponse;
import com.mongodb.api.hrms.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    AuthController authController;

    @Mock
    AuthService authService;

    @Test
    void login() {
        AuthRequest authRequest = new AuthRequest();

        authRequest.setUsername("john.doe");
        authRequest.setPassword("j0hn.d03");

        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSIsInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNzQ3NTM4" +
                "NTk4LCJleHAiOjE3NDc1NDIxOTh9.16fKiipBYSGIMkjfFNJQZ2Af6GHF_x3Kw1kTg-Tj0tY";

        AuthResponse authResponse = new AuthResponse(token);

        when(authService.login(authRequest)).thenReturn(authResponse);

        AuthResponse result = authController.login(authRequest);

        verify(authService).login(authRequest);
        assertEquals(authResponse, result);
    }
}
