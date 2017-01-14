package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.WaitMonitor;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import rx.Subscription;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata4FilteringObservable {

    private final WikiService wikiService = new WikiService();

    @Test
    public void filterObservable() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console


        WaitMonitor waitMonitor = new WaitMonitor();
        Subscription sub = wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
            .filter(article -> article.length() >= 15)
            .subscribe(
                    n -> System.out.println("Article: " + n),
                    err -> System.err.println("Error: " + err.getMessage()),
                    () -> {
                        System.out.println("Done");
                        waitMonitor.complete();
                    }
            );

        waitMonitor.waitFor(3, TimeUnit.SECONDS);
        sub.unsubscribe();
    }

    @Test
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)

        WaitMonitor waitMonitor = new WaitMonitor();
        Subscription sub = wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS)
                .sample(500, TimeUnit.MILLISECONDS)
                .subscribe(
                        n -> System.out.println("Article: " + n),
                        err -> System.err.println("Error: " + err.getMessage()),
                        () -> {
                            System.out.println("Done");
                            waitMonitor.complete();
                        }
                );

        waitMonitor.waitFor(3, TimeUnit.SECONDS);
        sub.unsubscribe();
    }
}
