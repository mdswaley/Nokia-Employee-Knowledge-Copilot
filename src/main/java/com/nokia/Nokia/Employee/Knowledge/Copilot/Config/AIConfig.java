package com.nokia.Nokia.Employee.Knowledge.Copilot.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

//    We can use this chat for temporary data storage.
    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory
                .builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();
    }


}
