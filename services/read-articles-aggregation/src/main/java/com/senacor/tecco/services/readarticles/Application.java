package com.senacor.tecco.services.readarticles;

import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.services.readarticles.aggregation.AggregationEndpoint;
import com.senacor.tecco.services.readarticles.external.MetricsService;
import com.senacor.tecco.services.readarticles.external.MetricsServiceImpl;
import com.senacor.tecco.services.readarticles.external.WikiLoaderService;
import com.senacor.tecco.services.readarticles.external.WikiLoaderServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
@SpringBootApplication
public class Application {

    @Bean
    public WikiLoaderService wikiLoaderService(@Value("${services.wikiloader}") String uri) {
        return new WikiLoaderServiceImpl(WebClient.create(uri));
    }

    @Bean
    public MetricsService metricsService(@Value("${services.metrics}") String uri) {
        return new MetricsServiceImpl(WebClient.create(uri));
    }

    @Bean
    public AggregationEndpoint endpoint() {
        Flux<String> names = WikiService.create()
                                        .wikiArticleBeingReadFluxBurstOwn()
                                        .onBackpressureDrop();
        return new AggregationEndpoint(names, metricsService(null), wikiLoaderService(null));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
