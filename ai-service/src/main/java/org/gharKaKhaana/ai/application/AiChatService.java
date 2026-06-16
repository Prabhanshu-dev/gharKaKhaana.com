package org.gharKaKhaana.ai.application;

import org.gharKaKhaana.ai.application.dto.ChatRequest;
import org.gharKaKhaana.ai.application.dto.ChatResponse;

public interface AiChatService {
    ChatResponse processChat(Long userId, ChatRequest request);
}
