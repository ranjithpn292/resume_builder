package com.resumebuilder.auth_service.controller;

import com.resumebuilder.auth_service.ai.AiService;
import com.resumebuilder.auth_service.dto.ResumeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/improve")
    public ResponseEntity<String> improveResume(@RequestBody ResumeRequest request) {
        try {
            String improved = aiService.improveResume(request.getContent());
            System.out.println("📤 Request Body: " + request.getContent());

            return ResponseEntity.ok(improved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to improve resume");
        }
    }
}
