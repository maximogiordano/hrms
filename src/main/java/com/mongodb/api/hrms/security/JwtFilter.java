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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("java:S6212")
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * This method is called once per request. It attempts to extract and validate the JWT token, and if valid, sets the
     * authentication in the SecurityContext.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractToken(request);

        try {
            setAuthentication(token);
        } catch (Exception e) {
            log.warn(e.toString());
        }

        // Continue the filter chain (pass the request to the next filter)
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the request. The token is expected to be in the format:
     * "Bearer &lt;token&gt;"
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        return authHeader != null && authHeader.startsWith("Bearer ") ?
                authHeader.substring(7) :
                null;
    }

    /**
     * Sets the Spring Security authentication.
     */
    private void setAuthentication(String token) {
        // Check that the token exists and no authentication has been set yet
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Extract the username from the token (if it doesn't fail, the token is valid and hasn't expired yet)
            String username = jwtUtils.extractUsername(token);

            // Load the user's details from the database
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Create an authentication object with user details and authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // credentials are not needed for JWT-based auth
                    userDetails.getAuthorities()
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
