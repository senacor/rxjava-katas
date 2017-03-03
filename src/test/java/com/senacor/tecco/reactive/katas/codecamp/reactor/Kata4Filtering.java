package com.senacor.tecco.reactive.katas.codecamp.reactor;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata4Filtering {

    private final WikiService wikiService = WikiService.create();

    /**
     * 1. Use {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} that delivers a stream of wiki article
     * names being read
     * 2. Filter the names so that only articles with at least 15 characters long names are accepted and print
     * everything to the console
     */
    @Test
    public void filter() throws Exception {
        wikiService.wikiArticleBeingReadFlux(500, TimeUnit.MILLISECONDS);
    }

    /**
     * 1. Use  {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} that delivers a stream of
     * wiki article names being read
     * 2. The stream delivers to many article to be processed.
     * Limit the stream to one article in 500ms. Do not change the parameter
     * at {@link WikiService#wikiArticleBeingReadFlux(long, TimeUnit)} ;)
     */
    @Test
    public void filter2() throws Exception {

        wikiService.wikiArticleBeingReadFlux(100, TimeUnit.MILLISECONDS);
    }
}