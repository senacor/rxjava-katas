package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import reactor.core.publisher.Mono;

/**
 * @author Daniel Heinrich
 */
public interface ArticleMetricsService {

    Mono<Integer> fetchWordCount(String articleName);

    Mono<Integer> fetchRating(String articleName);
}
