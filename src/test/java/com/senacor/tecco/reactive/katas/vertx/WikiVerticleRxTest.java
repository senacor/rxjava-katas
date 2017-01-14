package com.senacor.tecco.reactive.katas.vertx;

import com.senacor.tecco.reactive.ReactiveUtil;
import com.senacor.tecco.reactive.services.WikiService;
import de.tudarmstadt.ukp.wikipedia.parser.ParsedPage;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.test.core.VertxTestBase;
import org.junit.Before;
import org.junit.Test;
import rx.Observable;

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
      WikiService wikiService = new WikiService();

      vertxRx.eventBus().<String>sendObservable("article.fetch", "42")
          .subscribe(res -> {
            ReactiveUtil.print(res.body());
            assertThat(res.body(), containsString("42"));
          }, error -> {
            error.printStackTrace();
            fail("failed: " + error);
          });

      String testArticle = wikiService.fetchArticle("Boeing 777");
      vertxRx.eventBus().<String>sendObservable("article.parse", testArticle)
        .subscribe(res -> {
          ReactiveUtil.print(res.body());
        }, error -> {
          error.printStackTrace();
          fail("failed: " + error);
        });

      ParsedPage testPage = wikiService.parseMediaWikiText(testArticle);

      vertxRx.eventBus().<ParsedPage>sendObservable("article.count", testPage)
        .subscribe(res -> {
          ReactiveUtil.print(res.body());
        }, error -> {
          error.printStackTrace();
          fail("failed: " + error);
        });

      vertxRx.eventBus().<ParsedPage>sendObservable("article.rate", testPage)
        .subscribe(res -> {
          ReactiveUtil.print(res.body());
          testComplete();
        }, error -> {
          error.printStackTrace();
          fail("failed: " + error);
        });

        await(5, TimeUnit.SECONDS);
    }

  @Test
  public void testJSONCombining() throws Exception {

    vertxRx.deployVerticle(WikiVerticle.class.getName(), new DeploymentOptions().setWorker(true));
    waitUntil(() -> vertxRx.deploymentIDs().size() == 1);
    System.out.println("deployed verticles:" + vertx.deploymentIDs());

    vertxRx.eventBus().sendObservable("article.fetch", "42")
      .map(Message::body)
      .flatMap(article -> vertxRx.eventBus().sendObservable("article.parse", article))
      .map(Message::body)
      .flatMap(page -> {

        Observable<Message<Integer>> obsRating = vertxRx.eventBus().sendObservable("article.rate", page);
        Observable<Message<Integer>> obsCount = vertxRx.eventBus().sendObservable("article.count", page);

        return Observable.zip(obsRating, obsCount, (s1, s2) -> new JsonObject().put("rating", s1.body()).put("wordCount", s2.body()));
      })
      .subscribe(
        n -> System.out.println("JSON: " + n.encodePrettily()),
        err -> System.err.println("Error: " + err.getMessage()),
        () -> {
          testComplete();
          System.out.println("Done");
        }
      );
    await(5, TimeUnit.SECONDS);
  }
}
