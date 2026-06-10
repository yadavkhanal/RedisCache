package com.yadavkhanal.rediscache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.concurrent.*;

@Configuration
public class AppConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://jsonmock.hackerrank.com/api")
                .build();
    }

    @Bean
    public Executor limitedVirtualThreadExecutor() {
        return new ThreadPoolExecutor(
                50,  // max concurrency
                50,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Thread.ofVirtual().factory()
        );
    }
}