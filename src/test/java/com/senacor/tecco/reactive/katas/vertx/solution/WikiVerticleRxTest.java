package com.senacor.tecco.reactive.katas.vertx.solution;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.vertx.RxVertx;
import io.reactivex.Observable;
import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class WikiVerticleRxTest extends VertxTestBase {

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
                    ReactiveUtil.print(res);
                    testComplete();
                }, error -> {
                    error.printStackTrace();
                    fail("failed: " + error);
                });

        await(5, TimeUnit.SECONDS);
    }
}
