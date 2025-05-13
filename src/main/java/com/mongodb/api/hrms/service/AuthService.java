package com.mongodb.api.hrms.service;

import com.mongodb.api.hrms.dto.AuthRequest;
import com.mongodb.api.hrms.dto.AuthResponse;
import com.mongodb.api.hrms.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@SuppressWarnings("java:S6212")
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponse login(@Valid AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);

        authentication = authenticationManager.authenticate(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        return new AuthResponse(token);
    }
}
