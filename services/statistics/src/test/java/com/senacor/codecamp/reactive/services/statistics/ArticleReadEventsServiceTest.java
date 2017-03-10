package com.senacor.codecamp.reactive.services.statistics;

import com.senacor.codecamp.reactive.services.statistics.external.ArticleReadEventsService;
import com.senacor.codecamp.reactive.services.statistics.external.ArticleReadEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.HeaderSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Andri Bremm
 */
public class ArticleReadEventsServiceTest {

    private ArticleReadEventsService articleReadEventsService;

    private HeaderSpec headerSpec;
    private UriSpec uriSpec;
    private WebClient webClient;

    @Before
    public void setUp() {
        headerSpec = mock(HeaderSpec.class, Mockito.RETURNS_SELF);
        uriSpec = mock(UriSpec.class, (invocation) -> headerSpec);
        webClient = mock(WebClient.class, (invocation) -> uriSpec);

        articleReadEventsService = new ArticleReadEventsService(webClient);
    }

    @Test
    public void fetchReadEvents() {
        Flux<ArticleReadEvent[]> flux = Flux.intervalMillis(30).take(3)
                .map(count -> asList(createReadEvent(count)).toArray(new ArticleReadEvent[]{}));
        ClientResponse clientResponse = mock(ClientResponse.class);
        when(clientResponse.bodyToFlux(ArticleReadEvent[].class)).thenReturn(flux);
        when(headerSpec.exchange()).thenReturn(Mono.just(clientResponse));

        Flux<ArticleReadEvent> result = articleReadEventsService.readEvents();

        StepVerifier.create(result)
                .expectNext(createReadEvent(0l))
                .expectNext(createReadEvent(1l))
                .expectNext(createReadEvent(2l))
                .verifyComplete();
    }

    private ArticleReadEvent createReadEvent(Long number) {
        return new ArticleReadEvent("name", 100);
    }

}
