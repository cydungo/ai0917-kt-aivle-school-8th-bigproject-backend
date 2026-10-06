package com.aivle.ai0917.ipai.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.support.DefaultClientCodecConfigurer;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebAiConfig {
    @Value("${ai.server.base-url}")
    private String aiBaseUrl;

    @Value("${ai.server.max-in-memory-size-mb:20}")
    private int maxInMemorySizeMb;

    @Bean
    public WebClient aiWebClient() {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(this::configureCodecs)
                .build();

        return WebClient.builder()
                .baseUrl(aiBaseUrl)
                .exchangeStrategies(exchangeStrategies)
                .build();
    }

    private void configureCodecs(DefaultClientCodecConfigurer codecConfigurer) {
        codecConfigurer.defaultCodecs().maxInMemorySize(maxInMemorySizeMb * 1024 * 1024);
    }
}
