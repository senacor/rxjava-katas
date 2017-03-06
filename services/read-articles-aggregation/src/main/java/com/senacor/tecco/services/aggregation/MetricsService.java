package com.senacor.tecco.services.aggregation;

import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.senacor.tecco.services.aggregation.WrongStatusException.okFilter;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public class MetricsService {

    public static final String WORDCOUNT_ENDPOINT = "/metrics/wordcount";
    public static final String RATING_ENDPOINT = "/metrics/rating";
    private final WebClient metricsClient;

    public MetricsService(WebClient metricsClient) {
        this.metricsClient = metricsClient;
    }

    public Flux<Integer> fetchWordcount(String articleText) {
        return fetchWordcount(Mono.just(articleText));
    }

    public Flux<Integer> fetchWordcount(Publisher<String> articleText) {
        return metricsClient.post()
                            .uri(WORDCOUNT_ENDPOINT)
                            .exchange(articleText, String.class)
                            .doOnNext(okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt);
    }

    public Flux<Integer> fetchRating(String articleText) {
        return fetchRating(Mono.just(articleText));
    }

    public Flux<Integer> fetchRating(Publisher<String> articleText) {
        return metricsClient.post()
                            .uri(RATING_ENDPOINT)
                            .exchange(articleText, String.class)
                            .doOnNext(okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt);
    }
}
