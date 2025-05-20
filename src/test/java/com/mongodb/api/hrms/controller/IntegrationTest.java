package com.mongodb.api.hrms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.api.hrms.dto.AuthRequest;
import com.mongodb.api.hrms.dto.AuthResponse;
import com.mongodb.api.hrms.model.User;
import com.mongodb.api.hrms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
abstract class IntegrationTest {
    @Autowired
    PasswordEncoder passwordEncoder;

    @MockitoBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    String performSuccessfulLogin(String username, String password, String role) throws Exception {
        User user = new User();

        user.setId(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        when(userRepository.findById(username)).thenReturn(Optional.of(user));

        AuthRequest authRequest = new AuthRequest();

        authRequest.setUsername(username);
        authRequest.setPassword(password);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(response, AuthResponse.class).getToken();
    }
}
