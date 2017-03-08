package com.senacor.tecco.codecamp.reactive.services.statistics;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author Andri Bremm
 */
@SpringBootApplication
public class StatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class, args);
    }

    @Value("${services.article.base-url}")
    private String articleBaseUri;

    @Bean
    public WebClient webClient() {
        return WebClient.create(articleBaseUri);
    }

}
