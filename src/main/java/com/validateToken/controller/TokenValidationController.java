package com.validateToken.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RestController
public class TokenValidationController {

    private final WebClient webClient;
    private final String userInfoUri;

    public TokenValidationController(
            WebClient.Builder webClientBuilder,
            @Value("${cognito.user-info-uri}") String userInfoUri
    ) {
        this.webClient    = webClientBuilder.build();
        this.userInfoUri = userInfoUri;
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<?>> validate(@RequestHeader("Authorization") String auth) {
        if (!auth.startsWith("Bearer ")) {
            return Mono.just(ResponseEntity.badRequest().body("Missing Bearer token"));
        }
        return webClient
                .get()
                .uri(userInfoUri)
                .header(HttpHeaders.AUTHORIZATION, auth)
                .retrieve()
                .toEntity(JsonNode.class)
                .map(resp -> ResponseEntity.ok(Map.of(
                        "active", true,
                        "userInfo", resp.getBody()
                )))
                .onErrorResume(x -> Mono.just(ResponseEntity.status(401)
                        .body(Map.of("active", false, "error", x.getMessage()))));
    }
}