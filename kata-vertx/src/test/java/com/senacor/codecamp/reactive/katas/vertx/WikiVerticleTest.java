package com.senacor.codecamp.reactive.katas.vertx;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * @author Andreas Keefer
 */
public class WikiVerticleTest extends VertxTestBase {

    @Test
    public void testfetchArticle() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.eventBus().send("fetchArticle", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
            assertThat(res.result().body().toString(), containsString("42"));
            testComplete();
        });
        await(5, TimeUnit.SECONDS);
    }
}
