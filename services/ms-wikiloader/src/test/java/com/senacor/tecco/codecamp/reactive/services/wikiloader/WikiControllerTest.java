package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.ArticleName;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Rating;
import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.WordCount;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.ReactiveUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

import static com.senacor.tecco.reactive.util.DelayFunction.withNoDelay;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Andreas Keefer
 */
public class WikiControllerTest {

    static final Article EIGENWERT_ARTICLE = Article.newBuilder()
            .withName("Eigenwert")
            .withContent("#REDIRECT [[Eigenwertproblem]]")
            .build();
    static final Article EIGENVEKTOR_ARTICLE = Article.newBuilder()
            .withName("Eigenvektor")
            .withContent("#REDIRECT [[Eigenwertproblem]]")
            .build();

    private WebTestClient testClient;

    @Before
    public void setUp() throws Exception {
        this.testClient = WebTestClient.bindToController(new WikiController(
                WikiService.create(withNoDelay()),
                CountService.create(withNoDelay()),
                RatingService.create(withNoDelay()),
                10)).build();
    }

    @Test
    public void fetchArticle() throws Exception {
        Article res = testClient.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value()
                .isEqualTo(EIGENWERT_ARTICLE)
                .getResponseBody();
        ReactiveUtil.print(res);
        assertThat(res.getFetchTimeInMillis()).isBetween(0, 1000);
    }

    @Test
    public void getWordCount() throws Exception {
        testClient.get().uri("/article/{name}/wordcount", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("2");
    }

    @Test
    public void countWords() throws Exception {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");
        StepVerifier.create(
                testClient.post().uri("/article/wordcounts")
                        //.contentType(mediaType)
                        .accept(mediaType)
                        .exchange(Flux.just(EIGENWERT_ARTICLE.toArticleName(), EIGENVEKTOR_ARTICLE.toArticleName())
                                .delayElements(Duration.ofMillis(10)), ArticleName.class)
                        .expectStatus().isOk()
                        .expectHeader().contentType(mediaType)
                        .expectBody(WordCount.class)
                        .returnResult()
                        .getResponseBody()
        ).expectNext(new WordCount(EIGENWERT_ARTICLE.getName(), 2), new WordCount(EIGENVEKTOR_ARTICLE.getName(), 2))
                .verifyComplete();
    }

    @Test
    public void getRating() throws Exception {
        testClient.get().uri("/article/{name}/rating", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("5");
    }

    @Test
    public void ratings() throws Exception {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");
        StepVerifier.create(
                testClient.post().uri("/article/ratings")
                        //.contentType(mediaType)
                        .accept(mediaType)
                        .exchange(Flux.just(EIGENWERT_ARTICLE.toArticleName(), EIGENVEKTOR_ARTICLE.toArticleName())
                                .delayElements(Duration.ofMillis(10)), ArticleName.class)
                        .expectStatus().isOk()
                        .expectHeader().contentType(mediaType)
                        .expectBody(Rating.class)
                        .returnResult()
                        .getResponseBody()
        ).expectNext(new Rating(EIGENWERT_ARTICLE.getName(), 5), new Rating(EIGENVEKTOR_ARTICLE.getName(), 5))
                .verifyComplete();
    }
}
