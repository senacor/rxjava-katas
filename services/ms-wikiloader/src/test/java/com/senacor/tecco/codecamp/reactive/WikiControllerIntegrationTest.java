package com.senacor.tecco.codecamp.reactive;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WikiControllerIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void testFetchArticle() throws Exception {
        this.client.get().uri("/article/Eigenwert")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value().isEqualTo("#REDIRECT [[Eigenwertproblem]]");
    }

}