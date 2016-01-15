package com.senacor.tecco.reactive.katas.vertx;

import com.senacor.tecco.reactive.ReactiveUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.test.core.VertxTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;

/**
 * @author Andreas Keefer
 */
public class WikiVerticleRxTest extends VertxTestBase {

    private Vertx vertxRx;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        vertxRx = new Vertx(vertx);
    }

    @Test
    public void testfetchArticle() throws Exception {
        vertxRx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertxRx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertxRx.eventBus().<String>sendObservable("fetchArticle", "42")
                .subscribe(res -> {
                    ReactiveUtil.print(res.body());
                    assertThat(res.body(), containsString("42"));
                    testComplete();
                }, error -> {
                    error.printStackTrace();
                    fail("failed: " + error);
                });

        await(5, TimeUnit.SECONDS);
    }
}
