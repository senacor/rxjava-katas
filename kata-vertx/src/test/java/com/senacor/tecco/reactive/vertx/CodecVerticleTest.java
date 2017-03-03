package com.senacor.tecco.reactive.vertx;

import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class CodecVerticleTest extends VertxTestBase {

    @Test
    public void testCodec() throws Exception {
        vertx.deployVerticle(CodecVerticle.class.getName());
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());
        Thread.sleep(5000L);
        vertx.undeploy(CodecVerticle.class.getName());
    }
}