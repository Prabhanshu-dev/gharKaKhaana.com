package org.gharKaKhaana.ai.application;

import lombok.RequiredArgsConstructor;
import org.gharKaKhaana.ai.application.dto.ChatRequest;
import org.gharKaKhaana.ai.application.dto.ChatResponse;
import org.gharKaKhaana.ai.infrastructure.gateway.OpenAiGateway;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final OpenAiGateway openAiGateway;

    @Override
    public ChatResponse processChat(Long userId, ChatRequest request) {
        // We could also keep track of chat history per user here, either in DB or memory
        String reply = openAiGateway.generateResponse(request.getMessage());
        return new ChatResponse(reply);
    }
}
