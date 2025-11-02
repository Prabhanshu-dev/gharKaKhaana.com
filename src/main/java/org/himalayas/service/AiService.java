package org.himalayas.service;// Example usage in a service class
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
//import org.springframework.ai.openai.OpenAiChatClient;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final OpenAIClient client;
    // Configures using the `openai.apiKey`, `openai.orgId`, `openai.projectId`, `openai.webhookSecret` and `openai.baseUrl` system properties
// Or configures using the `OPENAI_API_KEY`, `OPENAI_ORG_ID`, `OPENAI_PROJECT_ID`, `OPENAI_WEBHOOK_SECRET` and `OPENAI_BASE_URL` environment variables
    public AiService() {
        // Load credentials from environment variables or system properties
        this.client = OpenAIOkHttpClient.fromEnv();
    }


    public String getAiResponse(String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_5)
                .build();

        ChatCompletion chatCompletion = client.chat().completions().create(params);

        // Extract and return the model's reply
        return chatCompletion.toString();
    }

}