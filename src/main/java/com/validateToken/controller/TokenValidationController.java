package com.validateToken.controller;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenValidationController {

    @GetMapping("/secure")
    public String secure(@AuthenticationPrincipal Jwt jwt) {
        // Extract your desired claim; adjust "username" as needed.
        String username = jwt.getClaimAsString("username");
        return "Hello, " + username;
    }
}