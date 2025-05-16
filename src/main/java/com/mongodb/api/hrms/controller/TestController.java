package com.mongodb.api.hrms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/encode")
    public String encode(@RequestParam("pass") String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
