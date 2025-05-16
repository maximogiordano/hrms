package com.mongodb.api.hrms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filter that validates incoming JWT tokens and sets the security context for authenticated users.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    /**
     * Called once per request to apply JWT authentication.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // extract the token from the Authorization header
        String token = getToken(request);

        if (token != null) {
            processToken(token);
        }

        // continue the filter chain
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        return authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }

    /**
     * Extracts the username from the token and sets the security context if the user is not already authenticated.
     */
    private void processToken(String token) {
        String username = jwtUtils.extractUsername(token); // this also validates the token

        // ensure the user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            setSecurityContext(token, username);
        }
    }

    private void setSecurityContext(String token, String username) {
        // load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // convert roles from the token into Spring Security authorities
        List<SimpleGrantedAuthority> authorities = jwtUtils.extractRoles(token)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // create an authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        // set it in the context
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
