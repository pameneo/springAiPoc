package com.pamscafe.mcpclientdemo.config;

import org.springframework.ai.chat.client.ChatClientBuilderCustomizer;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientBuilderCustomizerConfig {

    @Bean
    @ConditionalOnProperty(name="logging.llm", havingValue = "true")
    public ChatClientBuilderCustomizer loggerAdvisor() {
        return builder -> builder.defaultAdvisors(new SimpleLoggerAdvisor());
    }
}
