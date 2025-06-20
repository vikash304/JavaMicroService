package com.validateToken.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
public class HelloController {

    // open endpoint
    @GetMapping("/public/ping")
    public String ping() {
        return "pong";
    }

    // secured endpoint
    @GetMapping("/private/whoami")
    public String whoami(Principal principal) {
        // principal.getName() is the Cognito username (sub or cognito:username)
        return "Hello, " + principal.getName();
    }
}
