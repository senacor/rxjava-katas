package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import reactor.core.publisher.Mono;

/**
 * Created by Daniel Heinrich on 06/03/2017.
 */
public interface ArticleMetricsService {

    Mono<Integer> fetchWordCount(String articleName);

    Mono<Integer> fetchRating(String articleName);
}
