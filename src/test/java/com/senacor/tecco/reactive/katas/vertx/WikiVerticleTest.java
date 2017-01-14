package com.senacor.tecco.reactive.katas.vertx;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.test.core.VertxTestBase;
import org.junit.Test;
import rx.Observable;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * @author Andreas Keefer
 */
public class WikiVerticleTest extends VertxTestBase {

    @Test
    public void testArticle() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        WikiService wikiService = new WikiService();
        ParsedPage testPage = wikiService.parseMediaWikiText(wikiService.fetchArticle("Boeing 777"));

        vertx.eventBus().send("article.fetch", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
            assertThat(res.result().body().toString(), containsString("42"));
        });

        vertx.eventBus().send("article.parse", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
        });

        vertx.eventBus().send("article.count", testPage, res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print("Article count: " + res.result().body());
        });

        vertx.eventBus().send("article.rate", testPage, res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print("Article rate: " + res.result().body());
            testComplete();
        });

        await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testJSONCombining() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.eventBus().send("article.fetch", "42", res -> {

            if (res.succeeded()) {

                String article = (String) res.result().body();
                vertx.eventBus().send("article.parse", article, resArticle -> {

                    if (resArticle.succeeded()) {

                        ParsedPage page = (ParsedPage) resArticle.result().body();
                        vertx.eventBus().send("article.count", page, res2 -> {

                            if (res2.succeeded()) {

                                int count = (Integer) res2.result().body();
                                vertx.eventBus().send("article.rate", page, res3 -> {

                                    if (res3.succeeded()) {

                                        int rate = (Integer) res3.result().body();
                                        System.out.println(new JsonObject().put("rating", rate).put("countWord", count).encodePrettily());
                                        testComplete();

                                    } else {
                                        System.err.println("Error: " + res3.cause().getMessage());
                                    }
                                });
                            } else {
                                System.err.println("Error: " + res2.cause().getMessage());
                            }
                        });
                    } else {
                        System.err.println("Error: " + resArticle.cause().getMessage());
                    }
                });
            } else {
                System.err.println("Error: " + res.cause().getMessage());
            }
        });
        await(5, TimeUnit.SECONDS);
    }
}
