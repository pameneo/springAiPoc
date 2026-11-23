package com.pamscafe.mcpclientdemo.config;

import io.qdrant.client.QdrantClient;
import org.springframework.ai.chat.cache.semantic.SemanticCache;
import org.springframework.ai.chat.cache.semantic.SemanticCacheAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.qdrant.QdrantVectorStore;
import org.springframework.ai.vectorstore.redis.cache.semantic.DefaultSemanticCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    private Resource systemPromptTemplate;

    @Bean
    public ChatClient openAiRagChatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory,
                                          RetrievalAugmentationAdvisor retrievalAugmentationAdvisor,
                                          SemanticCacheAdvisor semanticCacheAdvisor,
                                          ToolCallbackProvider toolCallbackProvider) {
/*        var options = OpenAiChatOptions.builder()
                .model("gpt-5.4-mini")
                .temperature(0.8)
                .maxCompletionTokens(1000);*/

        return chatClientBuilder
                .defaultAdvisors(List.of(
                        retrievalAugmentationAdvisor,
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        semanticCacheAdvisor))
                .defaultTools(toolCallbackProvider)
               // .defaultOptions(options)
                .defaultSystem(systemPromptTemplate)
                .build();
    }

    @Bean
    public RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
                                                                     ChatClient.Builder chatClientBuilder) {
        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers(TranslationQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder.clone())
                        .targetLanguage("english").build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .vectorStore(vectorStore)
                        .topK(3)
                        .similarityThreshold(0.5)
                        .build()).build();
    }

    @Bean("cacheVectorStore")
    public VectorStore vectorStore(QdrantClient qdrantClient, EmbeddingModel embeddingModel){
        return QdrantVectorStore.builder(qdrantClient, embeddingModel)
                .collectionName("pamscafe-semantic-cache")
                .initializeSchema(true)
                .build();

    }

    @Bean
    public SemanticCache semanticCache(@Qualifier("cacheVectorStore") VectorStore vectorStore, EmbeddingModel embeddingModel){
        return DefaultSemanticCache.builder()
                .vectorStore(vectorStore)
                .embeddingModel(embeddingModel)
                .similarityThreshold(0.8)
                .build();

    }

    @Bean
    public SemanticCacheAdvisor semanticCacheAdvisor(SemanticCache semanticCache){
        return SemanticCacheAdvisor.builder()
                .cache(semanticCache)
                .build();
    }

}
