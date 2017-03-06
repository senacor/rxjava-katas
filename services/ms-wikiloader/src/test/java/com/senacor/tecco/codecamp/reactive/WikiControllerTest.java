package com.senacor.tecco.codecamp.reactive;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.senacor.tecco.reactive.util.DelayFunction.withNoDelay;

/**
 * @author Andreas Keefer
 */
public class WikiControllerTest {

    private WebTestClient client;


    @Before
    public void setUp() throws Exception {
        this.client = WebTestClient.bindToController(new WikiController(WikiService.create(withNoDelay()))).build();
    }


    @Test
    public void fetchArticle() throws Exception {
        this.client.get().uri("/article/Eigenwert")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("#REDIRECT [[Eigenwertproblem]]");
    }

}