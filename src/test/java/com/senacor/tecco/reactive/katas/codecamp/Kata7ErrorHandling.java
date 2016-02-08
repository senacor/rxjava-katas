package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

/**
 * @author Andreas Keefer
 */
public class Kata7ErrorHandling {

    private final WikiService wikiService = new WikiService();

    @Test
    public void errors() throws Exception {
        // 1. use WikiService#wikiArticleBeingReadObservableWithRandomErrors that creates a stream of wiki article names being read.
        // 2. filter burst.
        // 3. handle error: use retries with increasing delays
        // 4. if retries fail, terminate stream with a default

        wikiService.wikiArticleBeingReadObservableWithRandomErrors();
    }

}
