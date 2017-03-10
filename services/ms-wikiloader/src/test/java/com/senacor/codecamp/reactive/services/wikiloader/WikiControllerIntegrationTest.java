package com.senacor.codecamp.reactive.services.wikiloader;

import com.senacor.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.codecamp.reactive.services.wikiloader.model.ArticleName;
import com.senacor.codecamp.reactive.services.wikiloader.model.Rating;
import com.senacor.codecamp.reactive.services.wikiloader.model.WordCount;
import com.senacor.codecamp.reactive.util.ReactiveUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WikiControllerIntegrationTest {

    @LocalServerPort
    private int port;

    // alternativ zu dem manuellen setUp einfach einen fertigen WebTestClient injizieren lassen
    //@Autowired
    private WebTestClient testClient;
    private WebClient client;

    @Before
    public void setUp() throws Exception {
        String baseUrl = "http://localhost:" + this.port;
        this.testClient = WebTestClient.bindToServer()
                .baseUrl(baseUrl)
                .build();
        this.client = WebClient.create(baseUrl);
    }

    @Test
    public void testFetchArticle() throws Exception {
        testClient.get().uri("/article/{name}", WikiControllerTest.EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value()
                .isEqualTo(WikiControllerTest.EIGENWERT_ARTICLE);
    }

    @Test
    public void getWordCount() throws Exception {
        testClient.get().uri("/article/{name}/wordcount", WikiControllerTest.EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("2");
    }

    @Test
    public void countWords() throws Exception {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");
        Map<String, Integer> stringIntegerMap = testClient.post().uri("/article/wordcounts")
                //.contentType(mediaType)
                .accept(mediaType)
                .exchange(Flux.just(WikiControllerTest.EIGENWERT_ARTICLE.toArticleName(), WikiControllerTest.EIGENVEKTOR_ARTICLE.toArticleName())
                        .delayElements(Duration.ofMillis(50)), ArticleName.class)
                .expectStatus().isOk()
                .expectHeader().contentType(mediaType)
                .expectBody(WordCount.class)
                .returnResult()
                .getResponseBody()
                .cast(WordCount.class)
                .collectMap(WordCount::getArticleName, WordCount::getCount)
                .blockMillis(4000);
        assertThat(stringIntegerMap)
                .containsOnlyKeys(WikiControllerTest.EIGENWERT_ARTICLE.getName(), WikiControllerTest.EIGENVEKTOR_ARTICLE.getName())
                .containsValues(2);
    }

    @Test
    public void getRating() throws Exception {
        testClient.get().uri("/article/{name}/rating", WikiControllerTest.EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("5");
    }

    @Test
    public void ratings() throws Exception {
        MediaType mediaType = MediaType.valueOf(MediaType.APPLICATION_STREAM_JSON_VALUE + ";charset=UTF-8");
        Map<String, Integer> stringIntegerMap = testClient.post().uri("/article/ratings")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(mediaType)
                .exchange(Flux.just(WikiControllerTest.EIGENWERT_ARTICLE.toArticleName(), WikiControllerTest.EIGENVEKTOR_ARTICLE.toArticleName()), ArticleName.class)
                .expectStatus().isOk()
                .expectHeader().contentType(mediaType)
                .expectBody(Rating.class)
                .returnResult()
                .getResponseBody()
                .cast(Rating.class)
                .collectMap(Rating::getArticleName, Rating::getRating)
                .blockMillis(4000);

        assertThat(stringIntegerMap)
                .containsOnlyKeys(WikiControllerTest.EIGENWERT_ARTICLE.getName(), WikiControllerTest.EIGENVEKTOR_ARTICLE.getName())
                .containsValues(5);
    }

    @Test(timeout = 5000)
    public void readevents() throws Exception {
        StepVerifier.create(
                client.get().uri("/article/readevents", WikiControllerTest.EIGENWERT_ARTICLE.getName())
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .accept(MediaType.APPLICATION_STREAM_JSON)
                        .exchange()
                        .flatMap(clientResponse -> clientResponse.bodyToFlux(Article.class))
                        .doOnNext(next -> ReactiveUtil.print("received readevent in testclient: %s", next))
                        .next()
                        .doOnSubscribe(subscription -> {
                            // call fetchArticle
                            Mono.delayMillis(50)
                                    .flatMap(delay -> client.get().uri("/article/{name}", WikiControllerTest.EIGENWERT_ARTICLE.getName())
                                            .exchange())
                                    .log()
                                    .subscribe();
                        }).log()
        ).expectNextMatches(article -> article.getFetchTimeInMillis() != null)
                .verifyComplete();
    }
}