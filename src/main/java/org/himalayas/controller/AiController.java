package org.himalayas.controller;

import org.himalayas.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {
    @Autowired
    private AiService aiService;

    @PostMapping("/chat")
    public String chat(@RequestBody String prompt) {
        return aiService.getAiResponse(prompt);
    }
}

