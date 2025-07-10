package com.resumebuilder.auth_service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    @Value("${openai.api.key}")
    private String openaiKey;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String improveResume(String resumeData) throws IOException {

        System.out.println("🔑 OpenAI Key: " + openaiKey);
        // Build the OpenAI JSON request
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "gpt-3.5-turbo");

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Improve this resume: " + resumeData);

        requestMap.put("messages", List.of(message));

        String requestBodyJson = mapper.writeValueAsString(requestMap);

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(requestBodyJson, mediaType);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer " + openaiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("openAPI response::" + response + "," + response.isSuccessful());
            if (!response.isSuccessful()) {
                System.out.println("❌ OpenAI API error: " + response.code() + " - " + response.message());
                System.out.println("🔴 Response body: " + response.body().string());
                throw new IOException("OpenAI API call failed");
            }
            return response.body().string();
        }
    }
}
