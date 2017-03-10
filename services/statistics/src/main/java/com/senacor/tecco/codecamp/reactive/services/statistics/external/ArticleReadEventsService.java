package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import reactor.core.publisher.Flux;

/**
 * @author Andri Bremm
 */
public interface ArticleReadEventsService {

    Flux<ArticleReadEvent> readEvents();
}
