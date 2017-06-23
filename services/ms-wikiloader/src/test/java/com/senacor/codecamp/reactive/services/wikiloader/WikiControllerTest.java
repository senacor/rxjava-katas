package com.senacor.codecamp.reactive.services.wikiloader;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.codecamp.reactive.services.wikiloader.model.ArticleName;
import com.senacor.codecamp.reactive.services.wikiloader.model.Rating;
import com.senacor.codecamp.reactive.services.wikiloader.model.WordCount;
import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import static com.senacor.codecamp.reactive.util.DelayFunction.withNoDelay;
import static java.util.Arrays.asList;
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
        this.testClient = WebTestClient.bindToController(new WikiController(new ArticleService(
                WikiService.create(withNoDelay()),
                CountService.create(withNoDelay()),
                RatingService.create(withNoDelay()),
                10))).build();
    }

    @Test
    public void fetchArticle() throws Exception {
        Article res = testClient.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .isEqualTo(EIGENWERT_ARTICLE)
                .returnResult()
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
                .isEqualTo("2");
    }

    @Test
    public void countWords() throws Exception {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");
        Flux<WordCount> wordCountResult = testClient.post().uri("/article/wordcounts")
                .accept(mediaType)
                .body(Flux.just(EIGENWERT_ARTICLE.toArticleName(), EIGENVEKTOR_ARTICLE.toArticleName()), ArticleName.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(mediaType)
                .returnResult(WordCount.class)
                .getResponseBody();

        // We have to transform the list into an set to expect the word counts without an order.
        Flux<Set> resutAsSet = wordCountResult.bufferTimeout(2, Duration.ofMillis(100)).map(list -> new HashSet(list));

        Set<WordCount> expectedResult = new HashSet<>(asList(
                new WordCount(EIGENWERT_ARTICLE.getName(), 2),
                new WordCount(EIGENVEKTOR_ARTICLE.getName(), 2)));
        StepVerifier.create(resutAsSet)
                .expectNext(expectedResult)
                .verifyComplete();
    }

    @Test
    public void getRating() throws Exception {
        testClient.get().uri("/article/{name}/rating", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("5");
    }

    @Test
    public void ratings() throws Exception {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");
        Flux<Rating> ratingsResult = testClient.post().uri("/article/ratings")
                .accept(mediaType)
                .body(Flux.just(EIGENWERT_ARTICLE.toArticleName(), EIGENVEKTOR_ARTICLE.toArticleName()), ArticleName.class)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(mediaType)
                .returnResult(Rating.class)
                .getResponseBody();

        // We have to transform the list into an set to expect the word counts without an order.
        Flux<Set> resutAsSet = ratingsResult.bufferTimeout(2, Duration.ofMillis(100)).map(list -> new HashSet(list));

        Set<Rating> expectedResult = new HashSet<>(asList(
                new Rating(EIGENWERT_ARTICLE.getName(), 5),
                new Rating(EIGENVEKTOR_ARTICLE.getName(), 5)));
        StepVerifier.create(resutAsSet)
                .expectNext(expectedResult)
                .verifyComplete();
    }
}
