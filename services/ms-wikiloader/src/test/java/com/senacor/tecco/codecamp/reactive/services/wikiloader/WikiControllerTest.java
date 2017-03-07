package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

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
                RatingService.create(withNoDelay())
        )).build();
    }

    @Test
    public void fetchArticle() throws Exception {
        client.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Article.class)
                .value().isEqualTo(EIGENWERT_ARTICLE);
    }
}
