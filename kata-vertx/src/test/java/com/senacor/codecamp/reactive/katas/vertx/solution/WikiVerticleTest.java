package com.senacor.codecamp.reactive.katas.vertx.solution;

import com.senacor.codecamp.reactive.util.ReactiveUtil;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.DeploymentOptions;
import io.vertx.test.core.VertxTestBase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.*;

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

    @Test
    public void testParse() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.eventBus().send("fetchArticle", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
            assertThat(res.result().body().toString(), containsString("42"));
            vertx.eventBus().<ParsedPage>send("parseMediaWikiText", res.result().body(), parsed -> {
                ParsedPage parsedPage = parsed.result().body();
                assertThat(parsedPage, notNullValue());
                testComplete();
            });

        });
        await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testCountWords() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.eventBus().send("fetchArticle", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
            assertThat(res.result().body().toString(), containsString("42"));
            vertx.eventBus().<ParsedPage>send("parseMediaWikiText", res.result().body(), parsed -> {
                ParsedPage parsedPage = parsed.result().body();
                assertThat(parsedPage, notNullValue());
                vertx.eventBus().<Integer>send("countWords", parsedPage, resCount -> {
                    assertThat(resCount.result().body(),
                            allOf(greaterThanOrEqualTo(200), lessThanOrEqualTo(210)));
                    testComplete();
                });
            });

        });
        await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testRate() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        vertx.eventBus().send("fetchArticle", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
            assertThat(res.result().body().toString(), containsString("42"));
            vertx.eventBus().<ParsedPage>send("parseMediaWikiText", res.result().body(), parsed -> {
                ParsedPage parsedPage = parsed.result().body();
                assertThat(parsedPage, notNullValue());
                vertx.eventBus().<Integer>send("rate", parsedPage, resRate -> {
                    assertThat(resRate.result().body(), is(5));
                    testComplete();
                });
            });

        });
        await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testCombine() throws Exception {
        vertx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
        waitUntil(() -> vertx.deploymentIDs().size() == 1);
        System.out.println("deployed verticles:" + vertx.deploymentIDs());

        CountAndRate countAndRate = new CountAndRate();

        vertx.eventBus().send("fetchArticle", "42", res -> {
            assertThat(res.succeeded(), is(true));
            ReactiveUtil.print(res.result().body());
            assertThat(res.result().body().toString(), containsString("42"));
            vertx.eventBus().<ParsedPage>send("parseMediaWikiText", res.result().body(), parsed -> {
                ParsedPage parsedPage = parsed.result().body();
                assertThat(parsedPage, notNullValue());

                vertx.eventBus().<Integer>send("countWords", parsedPage, resCount -> {
                    countAndRate.setCount(resCount.result().body());
                });
                vertx.eventBus().<Integer>send("rate", parsedPage, resRate -> {
                    countAndRate.setRate(resRate.result().body());
                });
            });

        });
        waitUntil(() -> countAndRate.getCount() != null && countAndRate.getRate() != null);
        ReactiveUtil.print(countAndRate);
    }

    static class CountAndRate {
        private Integer count;
        private Integer rate;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Integer getRate() {
            return rate;
        }

        public void setRate(Integer rate) {
            this.rate = rate;
        }


        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append("count", count)
                    .append("rate", rate)
                    .toString();
        }
    }
}
