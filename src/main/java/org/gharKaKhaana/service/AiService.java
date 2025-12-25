package org.gharKaKhaana.service;// Example usage in a service class
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final OpenAIClient client;

    // Use Spring to inject the starter-provided OpenAIClient (configured from application.yml or env)
    public AiService(OpenAIClient client) {
        this.client = client;
    }


    public String getAiResponse(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_3_5_TURBO)
                .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);

        // Extract and return the model's reply (adjust to get the actual text if needed)
        return chatCompletion.toString();
    }

}