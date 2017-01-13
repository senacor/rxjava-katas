package com.senacor.tecco.reactive.katas.codecamp;

import com.senacor.tecco.reactive.services.CountService;
import com.senacor.tecco.reactive.services.RatingService;
import com.senacor.tecco.reactive.services.WikiService;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Andreas Keefer
 */
public class Kata5SchedulingObservable {

    private final WikiService wikiService = new WikiService();
    private final RatingService ratingService = new RatingService();
    private final CountService countService = new CountService();

    @Test
    public void schedulingObservable() throws Exception {
        // 1. use the WikiService#wikiArticleBeingReadObservable to create a stream of WikiArticle names being read
        // 2. take only the first 20 articles
        // 3. load and parse the article
        // 4. use the ratingService.rateObservable() and #countWordsObervable() to combine both as JSON
        //    and print the JSON to the console. Example {"rating": 3, "wordCount": 452}
        // 5. measure the runtime
        // 6. add a scheduler to a specific position in the observable chain to reduce the execution time

        wikiService.wikiArticleBeingReadObservable(50, TimeUnit.MILLISECONDS);
    }

}
