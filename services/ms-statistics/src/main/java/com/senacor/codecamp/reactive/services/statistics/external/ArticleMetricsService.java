package com.senacor.codecamp.reactive.services.statistics.external;

import com.senacor.codecamp.reactive.services.statistics.model.ArticleName;
import com.senacor.codecamp.reactive.services.statistics.model.Rating;
import com.senacor.codecamp.reactive.services.statistics.model.WordCount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Daniel Heinrich
 */
public interface ArticleMetricsService {

    Mono<Integer> fetchWordCount(String articleName);

    Mono<Integer> fetchRating(String articleName);

    Flux<WordCount> fetchWordCounts(Flux<ArticleName> articleName);

    Flux<Rating> fetchRatings(Flux<ArticleName> articleName);
}
