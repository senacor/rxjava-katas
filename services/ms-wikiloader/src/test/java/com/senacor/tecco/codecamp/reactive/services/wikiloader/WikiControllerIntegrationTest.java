package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.senacor.tecco.reactive.util.ReactiveUtil.print;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WikiControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void testFetchArticle() throws Exception {
        getClient().get().uri("/article/{name}", WikiControllerTest.EIGENWERT_ARTICLE.getName())

                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value()
                .isEqualTo(WikiControllerTest.EIGENWERT_ARTICLE);
    }

    @Test
    @Ignore("fails with 400 Bad Request")
    public void fetchArticles() throws Exception {
        FluxExchangeResult<Article> result = getClient().get().uri("/article/stream")
                .accept(TEXT_EVENT_STREAM)
                .exchange(Flux.intervalMillis(500).map(interval -> WikiControllerTest.EIGENWERT_ARTICLE.getName()).take(2), String.class)
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(Article.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody()
                .doOnNext(next -> print("next(Client): %s", next))
        ).expectNext(WikiControllerTest.EIGENWERT_ARTICLE, WikiControllerTest.EIGENWERT_ARTICLE)
                //.expectNextCount(5)
                .verifyComplete();
    }

    @Test
    @Ignore("fails with 400 Bad Request")
    public void fetchArticlesTest() throws Exception {
        FluxExchangeResult<Article> result = getClient().get().uri("/article/test")
                .accept(TEXT_EVENT_STREAM)
                .exchange(Mono.just(new String[]{WikiControllerTest.EIGENWERT_ARTICLE.getName(),
                                WikiControllerTest.EIGENWERT_ARTICLE.getName()}),
                        String[].class)
                .expectStatus().isOk()
                .expectHeader().contentType(TEXT_EVENT_STREAM)
                .expectBody(Article.class)
                .returnResult();

        StepVerifier.create(result.getResponseBody()
                .doOnNext(next -> print("next(Client): %s", next))
        ).expectNext(WikiControllerTest.EIGENWERT_ARTICLE, WikiControllerTest.EIGENWERT_ARTICLE)
                .verifyComplete();
    }

    protected WebTestClient getClient() {
        return client;
    }
}