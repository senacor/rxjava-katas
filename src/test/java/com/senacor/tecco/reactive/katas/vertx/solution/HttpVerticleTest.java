package com.senacor.tecco.reactive.katas.vertx.solution;

import io.vertx.core.DeploymentOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.test.core.VertxTestBase;
import org.apache.commons.httpclient.methods.GetMethod;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class HttpVerticleTest extends VertxTestBase {

    private Vertx vertxRx;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        vertxRx = new Vertx(vertx);
    }

    @Test
    public void start() throws Exception {
        vertxRx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        vertxRx.deployVerticle(HttpVerticle.class.getName());
        waitUntil(() -> vertxRx.deploymentIDs().size() == 2);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.createHttpClient().getNow(8081, "localhost", "/?articleName=42", response -> {

            assertEquals(200, response.statusCode());
            assertEquals("text/plain", response.headers().get("content-type"));
            response.bodyHandler(body -> {
                assertTrue(body.toString().contains("count=205 rate=5"));
                complete();
            });
        });
        await(5, TimeUnit.SECONDS);
    }
}
