package com.senacor.codecamp.reactive.services.statistics.external;

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
                .uri(ub -> ub.pathSegment(ARTICLE, READ_EVENTS).build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMap(response -> response.bodyToFlux(ArticleReadEvent[].class))
                .flatMap(articleReadEvents -> Flux.fromArray(articleReadEvents))
                .log();
    }
}
