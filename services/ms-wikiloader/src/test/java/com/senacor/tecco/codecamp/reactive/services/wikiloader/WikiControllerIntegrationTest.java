package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.util.ReactiveUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static com.senacor.tecco.codecamp.reactive.services.wikiloader.WikiControllerTest.EIGENWERT_ARTICLE;
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
        testClient.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value()
                .isEqualTo(EIGENWERT_ARTICLE);
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
    public void getRating() throws Exception {
        testClient.get().uri("/article/{name}/rating", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("5");
    }

    @Test
    public void readevents() throws Exception {
        StepVerifier.create(
                client.get().uri("/article/readevents", EIGENWERT_ARTICLE.getName())
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
                        .accept(MediaType.APPLICATION_STREAM_JSON)
                        .exchange()
                        .flatMap(clientResponse -> clientResponse.bodyToFlux(Article.class))
                        .doOnNext(next -> ReactiveUtil.print("received readevent in testclient: %s", next))
                        .next()
                        .doOnSubscribe(subscription -> {
                            // call fetchArticle
                            client.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                                    .exchange()
                                    .log()
                                    .subscribe();
                        }).log()
        ).expectNextMatches(article -> article.getFetchTimeInMillis() != null)
                .verifyComplete();
    }
}