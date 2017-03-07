package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.model.ReadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

/**
 * @author Andri Bremm
 */
@Service
public class ReadEventsService {

    @Value("${services.read-articles-aggregation}")
    private String uri;

    private WebClient webClient;

    @Autowired
    public ReadEventsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<ReadEvent> readEvents() {
        return webClient.get()
                .uri(URI.create(uri))
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .exchange().flatMap(response -> response.bodyToFlux(ReadEvent.class));
    }
}
