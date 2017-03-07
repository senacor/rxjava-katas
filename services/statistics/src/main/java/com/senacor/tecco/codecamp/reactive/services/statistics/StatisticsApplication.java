package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.external.MetricsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.MetricsServiceImpl;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.WikiLoaderService;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.WikiLoaderServiceImpl;
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

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    public WikiLoaderService wikiLoaderService(@Value("${services.wikiloader}") String uri) {
        return new WikiLoaderServiceImpl(WebClient.create(uri));
    }

    @Bean
    public MetricsService metricsService(@Value("${services.wikiloader}") String uri) {
        return new MetricsServiceImpl(WebClient.create(uri));
    }
}
