package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.apache.commons.collections4.map.LRUMap;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

import static com.senacor.tecco.reactive.util.DelayFunction.withNoDelay;

/**
 * @author Andreas Keefer
 */
public class WikiControllerTest {

    static final Article EIGENWERT_ARTICLE = new Article("Eigenwert", "#REDIRECT [[Eigenwertproblem]]");

    private WebTestClient client;

    @Before
    public void setUp() throws Exception {
        this.client = WebTestClient.bindToController(new WikiController(
                WikiService.create(withNoDelay()),
                CountService.create(withNoDelay()),
                RatingService.create(withNoDelay()),
                Collections.synchronizedMap(new LRUMap<>(1)))).build();
    }

    @Test
    public void fetchArticle() throws Exception {
        client.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value().isEqualTo(EIGENWERT_ARTICLE);
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