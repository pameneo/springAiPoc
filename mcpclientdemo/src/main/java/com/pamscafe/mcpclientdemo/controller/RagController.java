package com.pamscafe.mcpclientdemo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat/")
public class RagController {

    private final QdrantVectorStore vectorStore;
    private final ChatClient chatClient;

    public RagController(QdrantVectorStore vectorStore, @Qualifier("openAiRagChatClient") ChatClient chatClient) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
    }

    @GetMapping("/pantry")
    public ResponseEntity<String> randomChat(@RequestHeader("userId") String userId,
                                             @RequestParam("message") String message)
    {
        String answer = chatClient.prompt()
                .advisors(a -> a.param("chat_memory_conversation_id", userId))
                .user(message)
                .call()
                .content();

        return ResponseEntity.ok(answer);

    }
}
