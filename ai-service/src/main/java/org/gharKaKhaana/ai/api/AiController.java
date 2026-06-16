package org.gharKaKhaana.ai.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.ai.application.AiChatService;
import org.gharKaKhaana.ai.application.dto.ChatRequest;
import org.gharKaKhaana.ai.application.dto.ChatResponse;
import org.gharKaKhaana.ai.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiChatService aiChatService;

    @PostMapping("/chat")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestHeader("X-Auth-Role") String role,
            @Valid @RequestBody ChatRequest request) {
        
        enforceCustomerRole(role);
        
        ChatResponse response = aiChatService.processChat(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Success", response));
    }

    private void enforceCustomerRole(String role) {
        // As per requirements: "The OpenAI integration is strictly scoped to a customer-facing chat assistant for now."
        if (!"CUSTOMER".equalsIgnoreCase(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only customers can use the AI assistant.");
        }
    }
}
