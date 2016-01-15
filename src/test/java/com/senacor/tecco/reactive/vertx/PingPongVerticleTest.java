package com.senacor.tecco.reactive.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class PingPongVerticleTest extends VertxTestBase {

    @Test
    public void testPingPong() throws Exception {
        vertx.deployVerticle(PongVerticle.class.getName());
        vertx.deployVerticle(PingVerticle.class.getName(), new DeploymentOptions().setInstances(1).setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 2);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());
        Thread.sleep(5000L);
        vertx.undeploy(PingVerticle.class.getName());
        vertx.undeploy(PongVerticle.class.getName());
    }

}