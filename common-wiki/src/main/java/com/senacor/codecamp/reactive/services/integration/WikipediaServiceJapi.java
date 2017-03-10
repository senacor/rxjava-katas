package com.senacor.codecamp.reactive.services.integration;

import reactor.core.publisher.Mono;

/**
 * @author mmenzel
 */
public interface WikipediaServiceJapi {
    String getArticle(String name);

    /**
     * fetches a wiki article as a media wiki formatted string
     * <p>
     * This Impl is non-blocking and uses Netty to do the HTTP Call to Wikipedia
     *
     * @param name article name
     * @return fetches a wiki article as a media wiki formatted string
     */
    Mono<String> getArticleNonBlocking(String name);
}
