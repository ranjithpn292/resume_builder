package com.resumebuilder.auth_service.controller;

import com.resumebuilder.auth_service.dto.AuthResponse;
import com.resumebuilder.auth_service.dto.LoginRequest;
import com.resumebuilder.auth_service.dto.RegisterRequest;
import com.resumebuilder.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final RestTemplate restTemplate = new RestTemplate();
    private final WebClient webClient = WebClient.builder().baseUrl("https://dummyjson.com").build();

    @GetMapping("/call-external-rest")
    public ResponseEntity<String> getTodoListIt() throws Exception {
        String url = "https://dummyjson.com/todos";
        ResponseEntity<String> result;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("name", "Ranjith P N");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            result = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (Exception e) {
            throw new Exception("no response bud" + e.getMessage());
        }
        return result;
    }

    @GetMapping("/call-external-web")
    public Mono<String> getTodoList() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("name", "Ranjith P N");
        return webClient.get()
                .uri("/todos")
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .retrieve()
                .bodyToMono(String.class);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> placeOrder(@RequestBody String order) {
        kafkaTemplate.send("auth-topic", order);
        return ResponseEntity.ok("Auth-topic Order placed");
    }

    @PostMapping("/user")
    public ResponseEntity<String> placeUserOrder(@RequestBody String order) {
        kafkaTemplate.send("auth-topic", order);
        return ResponseEntity.ok("User-topic Order placed");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @Cacheable("users")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }
}
