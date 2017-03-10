package com.senacor.codecamp.reactive.services.wikiloader;

import com.senacor.codecamp.reactive.services.CountService;
import com.senacor.codecamp.reactive.services.RatingService;
import com.senacor.codecamp.reactive.services.WikiService;
import com.senacor.codecamp.reactive.services.wikiloader.service.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.senacor.codecamp.reactive.util.DelayFunction.withNoDelay;

/**
 * @author Andreas Keefer
 */
public class WikiControllerTest {

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

    }

}
