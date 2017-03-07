package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.senacor.tecco.codecamp.reactive.services.statistics.WrongStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class MetricsServiceImpl implements MetricsService {
    public static final String ARTICLE = "article";
    public static final String WORDCOUNT = "wordcount";
    public static final String RATING = "rating";
    private final WebClient metricsClient;

    public MetricsServiceImpl(WebClient metricsClient) {
        this.metricsClient = metricsClient;
    }

    @Override
    public Flux<Integer> fetchWordcount(String articleName) {
        return metricsClient.get()
                            .uri(ub -> ub.path(ARTICLE).path(articleName).path(WORDCOUNT).build())
                            .exchange()
                            .doOnNext(WrongStatusException.okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt);
    }

    @Override
    public Flux<Integer> fetchRating(String articleName) {
        return metricsClient.get()
                            .uri(ub -> ub.path(ARTICLE).path(articleName).path(RATING).build())
                            .exchange()
                            .doOnNext(WrongStatusException.okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt);
    }
}
