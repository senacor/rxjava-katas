package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;
import reactor.core.publisher.Flux;

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

        wikiService.wikiArticleBeingReadFlux(500, TimeUnit.MILLISECONDS);
    }

    @Test
    public void filterObservable2() throws Exception {
        // 1. Use WikiService#wikiArticleBeingReadObservable that delivers a stream of WikiArticle names being read
        // 2. The stream delivers to many article to be processed.
        //    Limit the stream to one article in 500ms. Do not change the parameter at wikiArticleBeingReadObservable ;)

        wikiService.wikiArticleBeingReadFlux(100, TimeUnit.MILLISECONDS);
    }
}
