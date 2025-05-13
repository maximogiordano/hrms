package com.mongodb.api.hrms.security;

import com.mongodb.api.hrms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(user -> new CustomUserDetails(username, user.getPassword(), user.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
