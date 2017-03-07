package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.model.ReadEvent;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

/**
 * @author Andri Bremm
 */
public class ReadEventsServiceTest {

    private ReadEventsService readEventsService;

    private HeaderSpec headerSpec;
    private UriSpec uriSpec;
    private WebClient webClient;

    @Before
    public void setUp() {
        headerSpec = mock(HeaderSpec.class, Mockito.RETURNS_SELF);
        uriSpec = mock(UriSpec.class, (invocation) -> headerSpec);
        webClient = mock(WebClient.class, (invocation) -> uriSpec);

        readEventsService = new ReadEventsService(webClient);
        setField(readEventsService, "uri", "/read");
    }

    @Test
    public void fetchReadEvents() {
        Flux<Object> flux = Flux.intervalMillis(30).take(3).map(count -> createReadEvent(count));
        ClientResponse clientResponse = mock(ClientResponse.class);
        when(clientResponse.bodyToFlux(any())).thenReturn(flux);
        when(headerSpec.exchange()).thenReturn(Mono.just(clientResponse));

        Flux<ReadEvent> result = readEventsService.readEvents();

        StepVerifier.create(result)
                .expectNext(createReadEvent(0l))
                .expectNext(createReadEvent(1l))
                .expectNext(createReadEvent(2l))
                .verifyComplete();
    }

    private ReadEvent createReadEvent(Long number) {
        return new ReadEvent("name", 100 * number.intValue(), number.intValue());
    }

}
