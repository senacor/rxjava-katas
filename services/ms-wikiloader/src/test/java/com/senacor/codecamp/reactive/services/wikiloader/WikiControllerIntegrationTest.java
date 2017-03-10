package com.senacor.codecamp.reactive.services.wikiloader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * @author Andreas Keefer
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class WikiControllerIntegrationTest {

    @LocalServerPort
    private int port;

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
    public void fetchArticle() throws Exception {

    }

    @Test
    public void getReadStream() throws Exception {

    }
}