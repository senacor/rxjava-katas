package com.senacor.tecco.codecamp.reactive.services.wikiloader;

import com.senacor.tecco.codecamp.reactive.services.wikiloader.model.Article;
import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import com.senacor.tecco.reactive.util.ReactiveUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

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

    private WebTestClient client;

    @Before
    public void setUp() throws Exception {
        this.client = WebTestClient.bindToController(new WikiController(
                WikiService.create(withNoDelay()),
                CountService.create(withNoDelay()),
                RatingService.create(withNoDelay()),
                10)).build();
    }

    @Test
    public void fetchArticle() throws Exception {
        Article res = client.get().uri("/article/{name}", EIGENWERT_ARTICLE.getName())
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
