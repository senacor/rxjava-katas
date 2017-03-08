package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.senacor.tecco.codecamp.reactive.services.wikiloader.WikiControllerTest.EIGENWERT_ARTICLE;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Primary
public class WikiControllerIntegrationTest {

    @LocalServerPort
    int port;

    // alternativ zu dem manuellen setUp einfach einen fertigen WebTestClient injizieren lassen
    //@Autowired
    private WebTestClient client;

    @Before
    public void setUp() throws Exception {
        this.client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + this.port)
                .build();
    }

    @Test
    public void testFetchArticle() throws Exception {
        client.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value()
                .isEqualTo(EIGENWERT_ARTICLE);
    }

    @Test
    public void getWordCount() throws Exception {
        client.get().uri("/article/{name}/wordcount", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("2");
    }

    @Test
    public void getRating() throws Exception {
        client.get().uri("/article/{name}/rating", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("5");
    }
}