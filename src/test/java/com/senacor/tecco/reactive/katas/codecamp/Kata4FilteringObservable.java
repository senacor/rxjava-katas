package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata4FilteringObservable {

    private final WikiService wikiService = new WikiService();

    @Test
    public void filterObservable() throws Exception {

        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        wikiService.wikiArticleBeingReadObservable(500, TimeUnit.MILLISECONDS)
        // 2. Filter the names so that only articles with at least 15 characters long names are accepted and print everything to the console
                .filter(x -> x.length() >= 15)
                .subscribe(
                        x -> System.out.println(x),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done")
                );

        Thread.sleep(10000);
    }

    @Test
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)

        wikiService.wikiArticleBeingReadObservable(100, TimeUnit.MILLISECONDS);
    }
}
