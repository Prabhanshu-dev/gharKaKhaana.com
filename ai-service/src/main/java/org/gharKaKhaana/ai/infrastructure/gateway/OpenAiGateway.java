package org.gharKaKhaana.ai.infrastructure.gateway;

import org.springframework.stereotype.Component;

/**
 * Mock implementation of OpenAI Gateway.
 * In Phase 5, this will be replaced with actual HTTP calls to OpenAI's API.
 */
@Component
public class OpenAiGateway {

    public String generateResponse(String prompt) {
        // Simulates an API call to OpenAI
        return "I am your GharKaKhaana AI assistant. You asked: '" + prompt + "'. How else can I help you discover great home-cooked meals today?";
    }
}
