package com.senacor.codecamp.reactive.katas.vertx.solution;

import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class HttpVerticleTest extends VertxTestBase {

    @Test
    public void start() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        vertx.deployVerticle(HttpVerticle.class.getName());
        waitUntil(() -> vertx.deploymentIDs().size() == 2);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.createHttpClient().getNow(8181, "localhost", "/?articleName=42", response -> {

            assertEquals(200, response.statusCode());
            assertEquals("text/plain", response.headers().get("content-type"));
            response.bodyHandler(body -> {
                assertTrue(body.toString().contains("count=20"));
                assertTrue(body.toString().contains(" rate=5"));
                complete();
            });
        });
        await(5, TimeUnit.SECONDS);
    }
}
