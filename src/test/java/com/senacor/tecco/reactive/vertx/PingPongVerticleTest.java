package com.senacor.tecco.reactive.vertx;

import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class PingPongVerticleTest extends VertxTestBase {

    @Test
    public void testPingPong() throws Exception {
        vertx.deployVerticle(PongVerticle.class.getName());
        vertx.deployVerticle(PingVerticle.class.getName());
        waitUntil(() -> vertx.deploymentIDs().size() == 2);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());
        Thread.sleep(5000L);
        vertx.undeploy(PingVerticle.class.getName());
        vertx.undeploy(PongVerticle.class.getName());
    }

}