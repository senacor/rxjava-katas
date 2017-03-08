package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import com.senacor.tecco.codecamp.reactive.services.statistics.WrongStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
@Service
public class ArticleMetricsServiceImpl implements ArticleMetricsService {

    public static final String ARTICLE = "article";
    public static final String WORD_COUNT = "wordcount";
    public static final String RATING = "rating";

    private final WebClient articleClient;

    @Autowired
    public ArticleMetricsServiceImpl(WebClient articleClient) {
        this.articleClient = articleClient;
    }

    @Override
    public Mono<Integer> fetchWordCount(String articleName) {
        return articleClient.get()
                            .uri(ub -> ub.pathSegment(ARTICLE, articleName, WORD_COUNT).build())
                            .exchange()
                            // TODO improve error handling in case article service is not available.
                            .doOnNext(WrongStatusException.okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt).single();
    }

    @Override
    public Mono<Integer> fetchRating(String articleName) {
        return articleClient.get()
                            .uri(ub -> ub.pathSegment(ARTICLE, articleName, RATING).build())
                            .exchange()
                            // TODO improve error handling in case article service is not available.
                            .doOnNext(WrongStatusException.okFilter())
                            .flatMap(r -> r.bodyToMono(String.class))
                            .map(Integer::parseInt).single();
    }
}
