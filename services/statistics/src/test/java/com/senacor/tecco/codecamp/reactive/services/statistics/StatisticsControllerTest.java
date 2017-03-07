package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleStatistics;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ReadEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

/**
 * @author Andri Bremm
 */
public class StatisticsControllerTest {

    private WebTestClient testClient;
    private ReadEventsService readEventsServiceMock = mock(ReadEventsService.class);

    @Before
    public void setUp() throws Exception {
        this.readEventsServiceMock = mock(ReadEventsService.class);
        this.testClient = WebTestClient.bindToController(new StatisticsController(readEventsServiceMock)).build();
    }

    @Test
    public void fetchArticleStatisticsWithDefaultUpdateInterval() {
        when(readEventsServiceMock.readEvents()).thenReturn(Flux.intervalMillis(245).take(6).map(this::createReadEvent));

        FluxExchangeResult<ArticleStatistics> result = testClient.get().uri("/statistics/article")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(ArticleStatistics.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody())
                .expectNext(new ArticleStatistics(4, 150.0, 1.5))
                .expectNext(new ArticleStatistics(2, 450.0, 4.5))
                .thenCancel()
                .verify();
    }

    @Test
    public void fetchArticleStatisticsWithShortUpdateInterval() {
        when(readEventsServiceMock.readEvents()).thenReturn(Flux.intervalMillis(400).take(4).map(this::createReadEvent));

        FluxExchangeResult<ArticleStatistics> result = testClient.get().uri("/statistics/article?updateInterval=500")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(ArticleStatistics.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody())
                .expectNextCount(1)
                .expectNext( new ArticleStatistics(1, 100.0, 1.0))
                .expectNext( new ArticleStatistics(1, 200.0, 2.0))
                .expectNext( new ArticleStatistics(1, 300.0, 3.0))
                .thenCancel()
                .verify();
    }

    private ReadEvent createReadEvent(Long count) {
        return new ReadEvent("name", 100 * count.intValue(), count.intValue());
    }

}
