package com.senacor.codecamp.reactive.katas.vertx.solution;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import com.senacor.codecamp.reactive.vertx.RxVertx;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.junit.After;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class WikiVerticleRxTest extends VertxTestBase {

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
        vertx.deploymentIDs().forEach(id -> vertx.undeploy(id));
    }

    @Test
    public void testfetchArticle() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        RxVertx.send(vertx, "fetchArticle", "42")
                .flatMap(article -> RxVertx.send(vertx, "parseMediaWikiText", article))
                .flatMap(parsedPage -> {
                    Observable<Integer> countWords = RxVertx.send(vertx, "countWords", parsedPage);
                    Observable<Integer> rate = RxVertx.send(vertx, "rate", parsedPage);
                    return Observable.zip(countWords, rate, (count, rateing) -> "count=" + count + " rate=" + rateing);
                })
                .subscribe(res -> {
                    ReactiveUtil.print("next: %s", res);
                    testComplete();
                }, error -> {
                    error.printStackTrace();
                    fail("failed: " + error);
                });

        await(10, TimeUnit.SECONDS);
    }
}
