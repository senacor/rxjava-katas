package com.senacor.codecamp.reactive.services.statistics.external;

import reactor.core.publisher.Mono;

/**
 * @author Daniel Heinrich
 */
public interface WikiLoaderService {

    Mono<Article> fetchArticle(String articleName);
}
