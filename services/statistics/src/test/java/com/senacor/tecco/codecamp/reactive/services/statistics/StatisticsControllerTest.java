package com.senacor.tecco.codecamp.reactive.services.statistics;

import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleMetricsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleReadEvent;
import com.senacor.tecco.codecamp.reactive.services.statistics.external.ArticleReadEventsService;
import com.senacor.tecco.codecamp.reactive.services.statistics.model.ArticleStatistics;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

/**
 * @author Andri Bremm
 */
public class StatisticsControllerTest {

    private ArticleReadEventsService articleReadEventsServiceMock;
    private ArticleMetricsService articleMetricsServiceMock;

    private WebTestClient testClient;

    @Before
    public void setUp() throws Exception {
        this.articleReadEventsServiceMock = mock(ArticleReadEventsService.class);
        this.articleMetricsServiceMock = mock(ArticleMetricsService.class);
        this.testClient = WebTestClient.bindToController(new StatisticsController(articleReadEventsServiceMock, articleMetricsServiceMock)).build();
    }

    @Test
    public void fetchArticleStatisticsWithDefaultUpdateInterval() {
        when(articleReadEventsServiceMock.readEvents()).thenReturn(Flux.intervalMillis(245).take(6).map(count -> createReadEvent(count, 100)));
        when(articleMetricsServiceMock.fetchRating(any())).thenAnswer(invocation -> Mono.just(Integer.parseInt(invocation.getArgument(0))));
        when(articleMetricsServiceMock.fetchWordCount(any())).thenAnswer(invocation -> Mono.just(Integer.parseInt(invocation.getArgument(0)) * 100));

        FluxExchangeResult<ArticleStatistics> result = testClient.get().uri("/statistics/article")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(ArticleStatistics.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody())
                .expectNext(new ArticleStatistics(4, 150.0, 1.5, 100.0))
                .expectNext(new ArticleStatistics(2, 450.0, 4.5, 100.0))
                .thenCancel()
                .verify();
    }

    @Test
    public void fetchArticleStatisticsWithShortUpdateInterval() {
        when(articleReadEventsServiceMock.readEvents()).thenReturn(Flux.intervalMillis(400).take(4).map(count -> createReadEvent(count, count.intValue())));
        when(articleMetricsServiceMock.fetchRating(any())).thenAnswer(invocation -> Mono.just(Integer.parseInt(invocation.getArgument(0))));
        when(articleMetricsServiceMock.fetchWordCount(any())).thenAnswer(invocation -> Mono.just(Integer.parseInt(invocation.getArgument(0)) * 100));


        FluxExchangeResult<ArticleStatistics> result = testClient.get().uri("/statistics/article?updateInterval=500")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(ArticleStatistics.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody())
                .expectNextCount(1)
                .expectNext(new ArticleStatistics(1, 100.0, 1.0, 1.0))
                .expectNext(new ArticleStatistics(1, 200.0, 2.0, 2.0))
                .expectNext(new ArticleStatistics(1, 300.0, 3.0, 3.0))
                .thenCancel()
                .verify();
    }

    private ArticleReadEvent createReadEvent(Long count, int fetchTimeInMillis) {
        return new ArticleReadEvent(count + "", fetchTimeInMillis);
    }

}
