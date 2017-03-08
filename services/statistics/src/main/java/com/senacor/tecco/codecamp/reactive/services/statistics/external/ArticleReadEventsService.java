package com.senacor.tecco.codecamp.reactive.services.statistics.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * @author Andri Bremm
 */
@Service
public class ArticleReadEventsService {

    public static final String ARTICLE = "article";
    private static final String READ_EVENTS = "readevents";

    private WebClient webClient;

    @Autowired
    public ArticleReadEventsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<ArticleReadEvent> readEvents() {
        return webClient.get()
                .uri(ub -> ub.path(ARTICLE).path(READ_EVENTS).build())
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .retry(3) // TODO improve error handling in case article service is not available.
                .flatMap(response -> response.bodyToFlux(ArticleReadEvent.class));
    }
}
