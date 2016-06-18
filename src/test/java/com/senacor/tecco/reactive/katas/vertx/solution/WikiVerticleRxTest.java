package com.senacor.tecco.reactive.katas.vertx.solution;

import com.senacor.tecco.reactive.ReactiveUtil;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.DeploymentOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.test.core.VertxTestBase;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

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
                .flatMap(article -> vertxRx.eventBus().<ParsedPage>sendObservable("parseMediaWikiText", article.body()))
                .map(Message::body)
                .flatMap(parsedPage -> {
                    Observable<Integer> countWords = vertxRx.eventBus().<Integer>sendObservable("countWords", parsedPage)
                            .map(Message::body);
                    Observable<Integer> rate = vertxRx.eventBus().<Integer>sendObservable("rate", parsedPage)
                            .map(Message::body);
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
