package com.paidyinc.forex.service.oneframe;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OneFrameConfiguration {

    @Bean
    public WebClient paidyOneFrameWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader("token", "10dc303535874aeccc86a8251e6992f5")
                .build();
    }
}
