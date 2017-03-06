package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.senacor.tecco.reactive.util.DelayFunction.withNoDelay;
import static com.senacor.tecco.reactive.util.ReactiveUtil.print;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

/**
 * @author Andreas Keefer
 */
public class WikiControllerTest {

    static final Article EIGENWERT_ARTICLE = new Article("Eigenwert", "#REDIRECT [[Eigenwertproblem]]");

    private WebTestClient client;

    @Before
    public void setUp() throws Exception {
        this.client = WebTestClient.bindToController(new WikiController(WikiService.create(withNoDelay()))).build();
    }

    @Test
    public void fetchArticle() throws Exception {
        getClient().get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value().isEqualTo(EIGENWERT_ARTICLE);
    }

    @Test
    public void fetchArticles() throws Exception {
        FluxExchangeResult<Article> result = getClient().get().uri("/article/stream")
                .accept(TEXT_EVENT_STREAM)
                .exchange(Flux.intervalMillis(500).map(interval -> EIGENWERT_ARTICLE.getName()).take(2), String.class)
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(Article.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody()
                .doOnNext(next -> print("next(Client): %s", next))
        ).expectNext(EIGENWERT_ARTICLE, EIGENWERT_ARTICLE)
                //.expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void fetchArticlesTest() throws Exception {
        FluxExchangeResult<Article> result = getClient().get().uri("/article/test")
                .accept(TEXT_EVENT_STREAM)
                .exchange(Mono.just(new String[]{EIGENWERT_ARTICLE.getName(),
                                EIGENWERT_ARTICLE.getName()}),
                        String[].class)
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(Article.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody()
                .doOnNext(next -> print("next(Client): %s", next))
        ).expectNext(EIGENWERT_ARTICLE, EIGENWERT_ARTICLE)
                .verifyComplete();
    }

    protected WebTestClient getClient() {
        return client;
    }
}